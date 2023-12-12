package com.osia.nota_maestro.controller.gradeSubject.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectDto
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectMapper
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectRequest
import com.osia.nota_maestro.service.gradeSubject.GradeSubjectService
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

@RestController("gradeSubject.v1.crud")
@CrossOrigin
@RequestMapping("v1/grade-subjects")
@Validated
class GradeSubjectController(
    private val gradeSubjectService: GradeSubjectService,
    private val gradeSubjectMapper: GradeSubjectMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<GradeSubjectDto> {
        return gradeSubjectService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<GradeSubjectDto> {
        return gradeSubjectService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return gradeSubjectService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<GradeSubjectDto> {
        return ResponseEntity.ok().body(gradeSubjectMapper.toDto(gradeSubjectService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<GradeSubjectDto>> {
        return ResponseEntity.ok().body(gradeSubjectService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: GradeSubjectRequest
    ): ResponseEntity<GradeSubjectDto> {
        return ResponseEntity(gradeSubjectService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody gradeSubjectRequestList: List<GradeSubjectRequest>
    ): ResponseEntity<List<GradeSubjectDto>> {
        return ResponseEntity(gradeSubjectService.saveMultiple(gradeSubjectRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: GradeSubjectRequest
    ): ResponseEntity<GradeSubjectDto> {
        return ResponseEntity.ok().body(gradeSubjectService.update(uuid, request,))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody gradeSubjectDtoList: List<GradeSubjectDto>
    ): ResponseEntity<List<GradeSubjectDto>> {
        return ResponseEntity.ok().body(gradeSubjectService.updateMultiple(gradeSubjectDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        gradeSubjectService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        gradeSubjectService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
