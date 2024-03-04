package com.osia.nota_maestro.controller.resource.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.resource.v1.ResourceDto
import com.osia.nota_maestro.dto.resource.v1.ResourceMapper
import com.osia.nota_maestro.dto.resource.v1.ResourceRequest
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.service.resource.ResourceService
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

@RestController("resource.v1.crud")
@CrossOrigin
@RequestMapping("v1/resources")
@Validated
class ResourceController(
    private val resourceService: ResourceService,
    private val resourceMapper: ResourceMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ResourceDto> {
        return resourceService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ResourceDto> {
        return resourceService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return resourceService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ResourceDto> {
        return ResponseEntity.ok().body(resourceMapper.toDto(resourceService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ResourceDto>> {
        return ResponseEntity.ok().body(resourceService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ResourceRequest,
        @RequestHeader school: UUID
    ): ResponseEntity<ResourceDto> {
        return ResponseEntity(resourceService.save(request, school), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody resourceRequestList: List<ResourceRequest>
    ): ResponseEntity<List<ResourceDto>> {
        return ResponseEntity(resourceService.saveMultiple(resourceRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ResourceRequest
    ): ResponseEntity<ResourceDto> {
        return ResponseEntity.ok().body(resourceService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody resourceDtoList: List<ResourceDto>
    ): ResponseEntity<List<ResourceDto>> {
        return ResponseEntity.ok().body(resourceService.updateMultiple(resourceDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        resourceService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        resourceService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/my/{uuid}")
    fun getResources(@PathVariable uuid: UUID): List<ResourceGradeDto> {
        return resourceService.my(uuid)
    }
}
