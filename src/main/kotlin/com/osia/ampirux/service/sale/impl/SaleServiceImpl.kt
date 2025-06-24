package com.osia.ampirux.service.sale.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.commission.v1.CommissionRequest
import com.osia.ampirux.dto.sale.v1.SaleDto
import com.osia.ampirux.dto.sale.v1.SaleRequest
import com.osia.ampirux.dto.saleproduct.v1.SaleProductDto
import com.osia.ampirux.dto.saleproduct.v1.SaleProductMapper
import com.osia.ampirux.dto.saleproduct.v1.SaleProductRequest
import com.osia.ampirux.dto.saleservice.v1.SaleServiceDto
import com.osia.ampirux.dto.saleservice.v1.SaleServiceMapper
import com.osia.ampirux.dto.saleservice.v1.SaleServiceRequest
import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductDto
import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductMapper
import com.osia.ampirux.dto.saleserviceproduct.v1.SaleServiceProductRequest
import com.osia.ampirux.model.Sale
import com.osia.ampirux.repository.sale.SaleRepository
import com.osia.ampirux.repository.saleproduct.SaleProductRepository
import com.osia.ampirux.repository.saleservice.SaleServiceRepository
import com.osia.ampirux.repository.saleserviceproduct.SaleServiceProductRepository
import com.osia.ampirux.service.barber.BarberService
import com.osia.ampirux.service.client.ClientService
import com.osia.ampirux.service.commission.CommissionService
import com.osia.ampirux.service.product.ProductService
import com.osia.ampirux.service.sale.SaleService
import com.osia.ampirux.service.saleproduct.SaleProductService
import com.osia.ampirux.service.saleservice.SaleServiceService
import com.osia.ampirux.service.saleserviceproduct.SaleServiceProductService
import com.osia.ampirux.service.service.ServiceService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class SaleServiceImpl(
    private val repository: SaleRepository,
    private val mapper: BaseMapper<SaleRequest, Sale, SaleDto>,
    private val saleServiceRepository: SaleServiceRepository,
    private val saleServiceMapper: SaleServiceMapper,
    private val saleServiceProductRepository: SaleServiceProductRepository,
    private val saleServiceProductMapper: SaleServiceProductMapper,
    private val saleProductRepository: SaleProductRepository,
    private val saleProductMapper: SaleProductMapper,
    private val saleServiceService: SaleServiceService,
    private val saleServiceProductService: SaleServiceProductService,
    private val saleProductService: SaleProductService,
    private val serviceService: ServiceService,
    private val productService: ProductService,
    private val clientService: ClientService,
    private val barberService: BarberService,
    private val commissionService: CommissionService,
    private val objectMapper: ObjectMapper
) : SaleService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Sale {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<SaleDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<SaleDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<Sale>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<SaleDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        val sales = repository.findAll(Specification.where(CreateSpec<Sale>().createSpec(where, barberShopUuid, listOf("code"))), pageable).map(mapper::toDto)
        val saleServices = saleServiceRepository.findAllBySaleUuidIn(sales.mapNotNull { it.uuid })
        val saleServiceProducts = saleServiceProductRepository.findAllBySaleServiceUuidIn(saleServices.mapNotNull { it.uuid })
        val saleProducts = saleProductRepository.findAllBySaleUuidIn(sales.mapNotNull { it.uuid })
        val services = serviceService.findByMultiple(saleServices.mapNotNull { it.serviceUuid }.distinct())
        val saleProductsIn = saleProducts.mapNotNull { it.productUuid } + saleServiceProducts.mapNotNull { it.productUuid }
        val products = productService.findByMultiple(saleProductsIn.distinct())
        val clients = clientService.findByMultiple(sales.mapNotNull { it.clientUuid }.distinct())
        val barbers = barberService.findByMultiple(sales.mapNotNull { it.barberUuid }.distinct())

        sales.forEach { s->
            s.services = saleServices.filter { ss-> ss.saleUuid == s.uuid }.map(saleServiceMapper::toDto)
            s.services?.forEach { ss->
                ss.service = services.firstOrNull { sx-> sx.uuid == ss.serviceUuid }
                ss.usedProducts = saleServiceProducts.filter { ssp -> ssp.saleServiceUuid == ss.uuid }.map(saleServiceProductMapper::toDto)
                ss.usedProducts?.forEach { sp ->
                    sp.product = products.firstOrNull { sx-> sx.uuid == sp.productUuid }
                }
            }
            s.products = saleProducts.filter { sp-> sp.saleUuid == s.uuid }.map(saleProductMapper::toDto)
            s.products?.forEach { sp ->
                sp.product = products.firstOrNull { sx-> sx.uuid == sp.productUuid }
            }

            s.client = clients.firstOrNull { c-> s.clientUuid == c.uuid }
            s.barber = barbers.firstOrNull { c-> s.barberUuid == c.uuid }
        }
        return sales;
    }

    @Transactional
    override fun save(request: SaleRequest, replace: Boolean): SaleDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request.apply {
            this.status = "PENDING"
        })
        val saved = mapper.toDto(repository.save(entity))
        request.services?.forEach { s ->
            val savedService = saleServiceService.save(s.apply { this.saleUuid = saved.uuid })
            s.usedProducts?.forEach { up ->
                up.saleServiceUuid = savedService.uuid
            }
            s.usedProducts?.let { saleServiceProductService.saveMultiple(it) }
        }
        request.products?.forEach { p ->
            p.saleUuid = saved.uuid
        }
        request.products?.let { saleProductService.saveMultiple(it) }


        return saved;
    }

    @Transactional
    override fun saveMultiple(requestList: List<SaleRequest>): List<SaleDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: SaleRequest, includeDelete: Boolean): SaleDto {
        log.trace("update -> id: $id, request: $request")

        val entity = if (!includeDelete) getById(id) else repository.getByUuid(id).get()
        mapper.update(request, entity)

        val saved = mapper.toDto(repository.save(entity))

        // === SYNC SERVICES ===
        val newServices = request.services ?: listOf()
        val existingServices = saleServiceRepository.findAllBySaleUuid(saved.uuid!!).map(saleServiceMapper::toDto)

        val existingMap = existingServices.associateBy { it.uuid }

        val toCreateServices = mutableListOf<SaleServiceRequest>()
        val toUpdateServices = mutableListOf<Pair<SaleServiceDto, SaleServiceRequest>>()
        val toDeleteServiceUuids = existingServices.mapNotNull { it.uuid }.toMutableSet()

        for (newService in newServices) {
            newService.saleUuid = saved.uuid
            if (newService.uuid != null && existingMap.containsKey(newService.uuid)) {
                toUpdateServices.add(existingMap[newService.uuid]!! to newService)
                toDeleteServiceUuids.remove(newService.uuid)
            } else {
                toCreateServices.add(newService)
            }
        }

        val createdServices = toCreateServices.map { saleServiceService.save(it) }
        val updatedServices = toUpdateServices.map { (old, req) -> saleServiceService.update(old.uuid!!, req) }
        saleServiceService.deleteMultiple(toDeleteServiceUuids.toList())

        val productsUsed: MutableMap<UUID, Double> = mutableMapOf()
        val products: MutableMap<UUID, Double> = mutableMapOf()

        // === SYNC USED PRODUCTS PER SERVICE ===
        (createdServices + updatedServices).forEach { service ->
            val usedProducts = newServices.find { it.uuid == service.uuid }?.usedProducts ?: listOf()
            usedProducts.forEach { it.saleServiceUuid = service.uuid }

            val existingUsed = saleServiceProductRepository.findAllBySaleServiceUuid(service.uuid!!).map(saleServiceProductMapper::toDto)
            val existingUsedMap = existingUsed.associateBy { it.uuid }

            val toCreate = mutableListOf<SaleServiceProductRequest>()
            val toUpdate = mutableListOf<SaleServiceProductDto>()
            val toDelete = existingUsed.mapNotNull { it.uuid }.toMutableSet()

            var discountCommission = 0.0
            for (new in usedProducts) {
                if (new.costType == "barber"){
                    discountCommission += (new.price ?: 0).toDouble()
                }
                if (new.uuid != null && existingUsedMap.containsKey(new.uuid)) {
                    toUpdate.add(SaleServiceProductDto().apply {
                        this.saleServiceUuid = service.uuid!!
                        this.costType = new.costType
                        this.unit = new.unit
                        this.productUuid = new.productUuid
                        this.uuid = new.uuid
                        this.quantity = new.quantity
                        this.price = new.price
                    })
                    toDelete.remove(new.uuid)
                } else {
                    toCreate.add(new)
                }
                if(new.productUuid != null) {
                    if(productsUsed.containsKey(new.productUuid)){
                        productsUsed[new.productUuid!!] = ((new.quantity ?: 0.0) + productsUsed[new.productUuid!!]!!);
                    }else{
                        productsUsed[new.productUuid!!] = (new.quantity ?: 0.0)
                    }
                }
            }

            saleServiceProductService.saveMultiple(toCreate)
            saleServiceProductService.updateMultiple(toUpdate)
            saleServiceProductService.deleteMultiple(toDelete.toList())

            if (request.status == "PAID" && request.commissions != null && request.barberUuid != null && request.barber != null && request.barber?.commissionRate != null) {
                commissionService.save(CommissionRequest().apply {
                    this.saleServiceUuid = service.uuid
                    this.barberUuid = request.barberUuid
                    this.status = "PENDING"
                    this.amount = (service.price?.times(BigDecimal.valueOf(request.barber!!.commissionRate!!))?.divide(
                        BigDecimal.valueOf(100)))?.minus(BigDecimal.valueOf(discountCommission));
                })
            }
        }

        // === SYNC SALE PRODUCTS ===
        val newProducts = request.products ?: listOf()
        newProducts.forEach { it.saleUuid = saved.uuid }

        val existingProducts = saleProductRepository.findAllBySaleUuid(saved.uuid!!).map(saleProductMapper::toDto)
        val existingProductMap = existingProducts.associateBy { it.uuid }

        val toCreate = mutableListOf<SaleProductRequest>()
        val toUpdate = mutableListOf<SaleProductDto>()
        val toDelete = existingProducts.mapNotNull { it.uuid }.toMutableSet()

        for (new in newProducts) {
            if (new.uuid != null && existingProductMap.containsKey(new.uuid)) {
                toUpdate.add(SaleProductDto().apply {
                    this.uuid = new.uuid
                    this.productUuid = new.productUuid
                    this.quantity = new.quantity
                    this.price = new.price
                    this.saleUuid = new.saleUuid
                    this.total = new.total
                })
                toDelete.remove(new.uuid)
            } else {
                toCreate.add(new)
            }
            if(new.productUuid != null) {
                if(products.containsKey(new.productUuid)){
                    products[new.productUuid!!] = ((new.quantity ?: 0.0) + products[new.productUuid!!]!!);
                }else{
                    products[new.productUuid!!] = (new.quantity ?: 0.0)
                }
            }
        }

        saleProductService.saveMultiple(toCreate)
        saleProductService.updateMultiple(toUpdate)
        saleProductService.deleteMultiple(toDelete.toList())

        if (request.status == "PAID") {
            removeStockProduct(productsUsed, products)
        }

        return saved
    }

    @Async
    override fun removeStockProduct(map: MutableMap<UUID, Double>, mapP: MutableMap<UUID, Double>){
        if(map.isEmpty() && mapP.isEmpty()) {
            return;
        }
        val products = productService.findByMultiple((map.keys.toList() + mapP.keys.toList()).distinct())
        products.forEach {
            if(it.stock != null && it.unit != null) {
                if(it.unit == "u") {
                    it.stock = (it.stock!!) - ((map[it.uuid!!] ?: 0.0) + (mapP[it.uuid!!] ?: 0.0))
                } else {
                    if(it.sizePerUnit != null && it.remainUnit != null) {
                        it.stock = it.stock!! - (mapP[it.uuid!!] ?: 0.0)
                        if (it.remainUnit!! < (map[it.uuid!!] ?: 0.0)) {
                            it.remainUnit = it.sizePerUnit!! - (((map[it.uuid!!] ?: 0.0).toInt()) - it.remainUnit!!)
                            it.stock = it.stock!! - 1
                        } else {
                            it.remainUnit = (it.remainUnit!!) - ((map[it.uuid!!] ?: 0.0).toInt())
                        }
                    }
                }
            }
        }
        productService.updateMultiple(products)
    }


    @Transactional
    override fun updateMultiple(dtoList: List<SaleDto>): List<SaleDto> {
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
