package com.osia.nota_maestro.controller.directorStudent.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentMapper
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentRequest
import com.osia.nota_maestro.service.directorStudent.DirectorStudentService
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

@RestController("directorStudent.v1.crud")
@CrossOrigin
@RequestMapping("v1/director-students")
@Validated
class DirectorStudentController(
    private val directorStudentService: DirectorStudentService,
    private val directorStudentMapper: DirectorStudentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<DirectorStudentDto> {
        return directorStudentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<DirectorStudentDto> {
        return directorStudentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return directorStudentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<DirectorStudentDto> {
        return ResponseEntity.ok().body(directorStudentMapper.toDto(directorStudentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<DirectorStudentDto>> {
        return ResponseEntity.ok().body(directorStudentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: DirectorStudentRequest
    ): ResponseEntity<DirectorStudentDto> {
        return ResponseEntity(directorStudentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody directorStudentRequestList: List<DirectorStudentRequest>
    ): ResponseEntity<List<DirectorStudentDto>> {
        return ResponseEntity(directorStudentService.saveMultiple(directorStudentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: DirectorStudentRequest
    ): ResponseEntity<DirectorStudentDto> {
        return ResponseEntity.ok().body(directorStudentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody directorStudentDtoList: List<DirectorStudentDto>
    ): ResponseEntity<List<DirectorStudentDto>> {
        return ResponseEntity.ok().body(directorStudentService.updateMultiple(directorStudentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        directorStudentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        directorStudentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
