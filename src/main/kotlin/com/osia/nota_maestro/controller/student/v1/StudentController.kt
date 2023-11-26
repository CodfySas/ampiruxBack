package com.osia.nota_maestro.controller.student.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.student.v1.StudentDto
import com.osia.nota_maestro.dto.student.v1.StudentMapper
import com.osia.nota_maestro.dto.student.v1.StudentRequest
import com.osia.nota_maestro.service.student.StudentService
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

@RestController("student.v1.crud")
@CrossOrigin
@RequestMapping("v1/students")
@Validated
class StudentController(
    private val studentService: StudentService,
    private val studentMapper: StudentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<StudentDto> {
        return studentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<StudentDto> {
        return studentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return studentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<StudentDto> {
        return ResponseEntity.ok().body(studentMapper.toDto(studentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<StudentDto>> {
        return ResponseEntity.ok().body(studentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: StudentRequest
    ): ResponseEntity<StudentDto> {
        return ResponseEntity(studentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody studentRequestList: List<StudentRequest>
    ): ResponseEntity<List<StudentDto>> {
        return ResponseEntity(studentService.saveMultiple(studentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: StudentRequest
    ): ResponseEntity<StudentDto> {
        return ResponseEntity.ok().body(studentService.update(uuid, request,))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody studentDtoList: List<StudentDto>
    ): ResponseEntity<List<StudentDto>> {
        return ResponseEntity.ok().body(studentService.updateMultiple(studentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        studentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        studentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
