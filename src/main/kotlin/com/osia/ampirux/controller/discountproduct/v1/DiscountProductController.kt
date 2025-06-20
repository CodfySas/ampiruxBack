package com.osia.ampirux.controller.discountproduct.v1
import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.discountproduct.v1.DiscountProductDto
import com.osia.ampirux.dto.discountproduct.v1.DiscountProductMapper
import com.osia.ampirux.dto.discountproduct.v1.DiscountProductRequest
import com.osia.ampirux.service.discountproduct.DiscountProductService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("discount-product.v1.crud")
@CrossOrigin
@RequestMapping("v1/discount-products")
@Validated
class DiscountProductController(
    private val service: DiscountProductService,
    private val mapper: DiscountProductMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page< DiscountProductDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String,
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID): Page< DiscountProductDto> {
        return service.findAllByFilter(pageable, where, barbershopUuid)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity< DiscountProductDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List< DiscountProductDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody request: DiscountProductRequest): ResponseEntity<DiscountProductDto > {
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody requestList: List< DiscountProductRequest>): ResponseEntity<List<DiscountProductDto > > {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: DiscountProductRequest): ResponseEntity<DiscountProductDto > {
        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List< DiscountProductDto>): ResponseEntity<List<DiscountProductDto > > {
        return ResponseEntity.ok(service.updateMultiple(dtoList))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        service.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(@RequestBody uuidList: List<UUID>): ResponseEntity<HttpStatus> {
        service.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
