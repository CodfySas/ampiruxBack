package com.osia.nota_maestro.controller.subject.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.subject.v1.SubjectDto
import com.osia.nota_maestro.dto.subject.v1.SubjectMapper
import com.osia.nota_maestro.dto.subject.v1.SubjectRequest
import com.osia.nota_maestro.service.subject.SubjectService
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

@RestController("subject.v1.crud")
@CrossOrigin
@RequestMapping("v1/subjects")
@Validated
class SubjectController(
    private val subjectService: SubjectService,
    private val subjectMapper: SubjectMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<SubjectDto> {
        return subjectService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<SubjectDto> {
        return subjectService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return subjectService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<SubjectDto> {
        return ResponseEntity.ok().body(subjectMapper.toDto(subjectService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<SubjectDto>> {
        return ResponseEntity.ok().body(subjectService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: SubjectRequest,
        @RequestHeader school: UUID
    ): ResponseEntity<SubjectDto> {
        return ResponseEntity(subjectService.save(request, school), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody subjectRequestList: List<SubjectRequest>
    ): ResponseEntity<List<SubjectDto>> {
        return ResponseEntity(subjectService.saveMultiple(subjectRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: SubjectRequest
    ): ResponseEntity<SubjectDto> {
        return ResponseEntity.ok().body(subjectService.update(uuid, request,))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody subjectDtoList: List<SubjectDto>
    ): ResponseEntity<List<SubjectDto>> {
        return ResponseEntity.ok().body(subjectService.updateMultiple(subjectDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        subjectService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        subjectService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
