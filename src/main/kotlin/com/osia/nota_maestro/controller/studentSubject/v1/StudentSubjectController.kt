package com.osia.nota_maestro.controller.studentSubject.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectMapper
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
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

@RestController("studentSubject.v1.crud")
@CrossOrigin
@RequestMapping("v1/student-subjects")
@Validated
class StudentSubjectController(
    private val studentSubjectService: StudentSubjectService,
    private val studentSubjectMapper: StudentSubjectMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<StudentSubjectDto> {
        return studentSubjectService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<StudentSubjectDto> {
        return studentSubjectService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return studentSubjectService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<StudentSubjectDto> {
        return ResponseEntity.ok().body(studentSubjectMapper.toDto(studentSubjectService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<StudentSubjectDto>> {
        return ResponseEntity.ok().body(studentSubjectService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: StudentSubjectRequest
    ): ResponseEntity<StudentSubjectDto> {
        return ResponseEntity(studentSubjectService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody studentSubjectRequestList: List<StudentSubjectRequest>
    ): ResponseEntity<List<StudentSubjectDto>> {
        return ResponseEntity(studentSubjectService.saveMultiple(studentSubjectRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: StudentSubjectRequest
    ): ResponseEntity<StudentSubjectDto> {
        return ResponseEntity.ok().body(studentSubjectService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody studentSubjectDtoList: List<StudentSubjectDto>
    ): ResponseEntity<List<StudentSubjectDto>> {
        return ResponseEntity.ok().body(studentSubjectService.updateMultiple(studentSubjectDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        studentSubjectService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        studentSubjectService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
