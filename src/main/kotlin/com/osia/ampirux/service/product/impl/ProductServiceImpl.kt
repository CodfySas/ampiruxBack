package com.osia.ampirux.service.product.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.product.v1.ProductDto
import com.osia.ampirux.dto.product.v1.ProductRequest
import com.osia.ampirux.dto.productcategory.v1.ProductCategoryDto
import com.osia.ampirux.dto.productcategory.v1.ProductCategoryRequest
import com.osia.ampirux.model.Product
import com.osia.ampirux.model.ProductCategory
import com.osia.ampirux.repository.product.ProductRepository
import com.osia.ampirux.repository.productcategory.ProductCategoryRepository
import com.osia.ampirux.service.product.ProductService
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
class ProductServiceImpl(
    private val repository: ProductRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val mapper: BaseMapper<ProductRequest, Product, ProductDto>,
    private val categoryMapper: BaseMapper<ProductCategoryRequest, ProductCategory, ProductCategoryDto>,
    private val objectMapper: ObjectMapper
) : ProductService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Product {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<ProductDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ProductDto> {
        log.trace("findAll -> pageable: $pageable")
        val products = repository.findAll(Specification.where(CreateSpec<Product>().createSpec("")), pageable).map(mapper::toDto)
        val productCategories = productCategoryRepository.findAllById(products.mapNotNull { it.categoryUuid }.distinct())
        products.forEach { p->
            p.category = p.categoryUuid?.let { productCategories.firstOrNull { pc-> pc.uuid == p.categoryUuid }
                ?.let { it1 -> categoryMapper.toDto(it1) } }
        }
        return products;
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<ProductDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        val products = repository.findAll(Specification.where(CreateSpec<Product>().createSpec(where, barberShopUuid, listOf("code", "name", "description", "unit"))), pageable).map(mapper::toDto)
        val productCategories = productCategoryRepository.findAllById(products.mapNotNull { it.categoryUuid }.distinct())
        products.forEach { p->
            p.category = p.categoryUuid?.let { productCategories.firstOrNull { pc-> pc.uuid == p.categoryUuid }
                ?.let { it1 -> categoryMapper.toDto(it1) } }
        }
        return products
    }

    @Transactional
    override fun save(request: ProductRequest, replace: Boolean): ProductDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun saveMultiple(requestList: List<ProductRequest>): List<ProductDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: ProductRequest, includeDelete: Boolean): ProductDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun updateMultiple(dtoList: List<ProductDto>): List<ProductDto> {
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
