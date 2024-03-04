package com.osia.nota_maestro.controller.mesh.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.mesh.v1.MeshDto
import com.osia.nota_maestro.dto.mesh.v1.MeshMapper
import com.osia.nota_maestro.dto.mesh.v1.MeshRequest
import com.osia.nota_maestro.service.mesh.MeshService
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

@RestController("mesh.v1.crud")
@CrossOrigin
@RequestMapping("v1/meshs")
@Validated
class MeshController(
    private val meshService: MeshService,
    private val meshMapper: MeshMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<MeshDto> {
        return meshService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<MeshDto> {
        return meshService.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return meshService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<MeshDto> {
        return ResponseEntity.ok().body(meshMapper.toDto(meshService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<MeshDto>> {
        return ResponseEntity.ok().body(meshService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: MeshRequest
    ): ResponseEntity<MeshDto> {
        return ResponseEntity(meshService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody meshRequestList: List<MeshRequest>
    ): ResponseEntity<List<MeshDto>> {
        return ResponseEntity(meshService.saveMultiple(meshRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: MeshRequest
    ): ResponseEntity<MeshDto> {
        return ResponseEntity.ok().body(meshService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody meshDtoList: List<MeshDto>
    ): ResponseEntity<List<MeshDto>> {
        return ResponseEntity.ok().body(meshService.updateMultiple(meshDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        meshService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        meshService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get/{classroom}/{subject}/{period}")
    fun getBy(@PathVariable classroom: UUID, @PathVariable subject: UUID, @PathVariable period: Int): MeshDto {
        return meshService.getBy(classroom,subject,period)
    }

    @GetMapping("/get-my/{uuid}/{subject}/{period}")
    fun getByMy(@PathVariable uuid: UUID, @PathVariable subject: UUID, @PathVariable period: Int): MeshDto {
        return meshService.getByMy(uuid, subject, period)
    }
}
