package com.osia.ampirux.controller.servicedefaultproduct.v1
import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductDto
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductMapper
import com.osia.ampirux.dto.servicedefaultproduct.v1.ServiceDefaultProductRequest
import com.osia.ampirux.service.servicedefaultproduct.ServiceDefaultProductService
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("service-default-products.v1.crud")
@CrossOrigin
@RequestMapping("v1/service-default-products")
@Validated
class ServiceDefaultProductController(
    private val service: ServiceDefaultProductService,
    private val mapper: ServiceDefaultProductMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page<ServiceDefaultProductDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<ServiceDefaultProductDto> {
        return service.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ServiceDefaultProductDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ServiceDefaultProductDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@Validated(OnCreate::class) request: ServiceDefaultProductRequest): ResponseEntity<ServiceDefaultProductDto> {
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@Validated(OnCreate::class) requestList: List<ServiceDefaultProductRequest>): ResponseEntity<List<ServiceDefaultProductDto> > {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: ServiceDefaultProductRequest): ResponseEntity<ServiceDefaultProductDto> {
        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List<ServiceDefaultProductDto>): ResponseEntity<List<ServiceDefaultProductDto> > {
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
