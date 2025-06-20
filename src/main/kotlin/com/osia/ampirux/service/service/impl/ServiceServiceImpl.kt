package com.osia.ampirux.service.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.product.v1.ProductMapper
import com.osia.ampirux.dto.service.v1.ServiceDto
import com.osia.ampirux.dto.service.v1.ServiceRequest
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductMapper
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductRequest
import com.osia.ampirux.repository.product.ProductRepository
import com.osia.ampirux.repository.service.ServiceRepository
import com.osia.ampirux.repository.servicedefaultproduct.ServiceDefaultProductRepository
import com.osia.ampirux.service.service.ServiceService
import com.osia.ampirux.service.servicedefaultproduct.ServiceDefaultProductService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class ServiceServiceImpl(
    private val repository: ServiceRepository,
    private val mapper: BaseMapper<ServiceRequest, com.osia.ampirux.model.Service, ServiceDto>,
    private val serviceDefaultProductService: ServiceDefaultProductService,
    private val serviceDefaultProductRepository: ServiceDefaultProductRepository,
    private val serviceDefaultProductMapper: ServiceDefaultProductMapper,
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val objectMapper: ObjectMapper
) : ServiceService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): com.osia.ampirux.model.Service {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<ServiceDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ServiceDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<com.osia.ampirux.model.Service>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<ServiceDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        val services = repository.findAll(Specification.where(CreateSpec<com.osia.ampirux.model.Service>().createSpec(where, barberShopUuid, listOf("code", "name", "description"))), pageable).map(mapper::toDto)
        val products = serviceDefaultProductRepository.getAllByServiceUuidIn(services.mapNotNull { it.uuid }.distinct()).map(serviceDefaultProductMapper::toDto)
        val pr = productRepository.findAllById(products.mapNotNull { it.productUuid }.distinct())
        products.forEach { p->
            p.product = pr.firstOrNull{ it.uuid == p.productUuid }?.let { productMapper.toDto(it) }
        }
        services.forEach { s->
            s.defaultProducts = products.filter { p-> p.serviceUuid == s.uuid }
        }
        return services
    }

    @Transactional
    override fun save(request: ServiceRequest, replace: Boolean): ServiceDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        val saved = mapper.toDto(repository.save(entity))
        val products = if(request.defaultProducts?.isNotEmpty() == true){
            serviceDefaultProductService.saveMultiple(request.defaultProducts!!.map{ df->
                ServiceDefaultProductRequest().apply {
                    this.productUuid = df.productUuid
                    this.serviceUuid = saved.uuid
                    this.costType = df.costType
                    this.quantity = df.quantity
                    this.unit = df.unit
                }
            })
        } else {
            mutableListOf()
        }
        return saved.apply { this.defaultProducts = products }
    }

    @Transactional
    override fun saveMultiple(requestList: List<ServiceRequest>): List<ServiceDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: ServiceRequest, includeDelete: Boolean): ServiceDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        val saved = mapper.toDto(repository.save(entity))
        val products = if(request.defaultProducts?.isNotEmpty() == true){
            serviceDefaultProductService.saveMultiple(request.defaultProducts!!.map{ df->
                ServiceDefaultProductRequest().apply {
                    this.productUuid = df.productUuid
                    this.serviceUuid = saved.uuid
                    this.costType = df.costType
                    this.quantity = df.quantity
                    this.unit = df.unit
                }
            })
        } else {
            mutableListOf()
        }
        return saved.apply { this.defaultProducts = products }
    }

    @Transactional
    override fun updateMultiple(dtoList: List<ServiceDto>): List<ServiceDto> {
        log.trace("updateMultiple -> dtoList: ${objectMapper.writeValueAsString(dtoList)}")
        val ids = dtoList.mapNotNull { it.uuid }
        val entities = repository.findAllById(ids)
        entities.forEach { entity ->
            val dto = dtoList.first { it.uuid == entity.uuid }
            mapper.update(mapper.toRequest(dto), entity)
        }
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun delete(id: UUID) {
        log.trace("delete -> id: $id")
        val entity = getById(id)
        entity.deleted = true
        entity.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        repository.save(entity)
    }

    @Transactional
    override fun deleteMultiple(idList: List<UUID>) {
        log.trace("deleteMultiple -> idList: $idList")
        val entities = repository.findAllById(idList)
        entities.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        }
        repository.saveAll(entities)
    }
}
