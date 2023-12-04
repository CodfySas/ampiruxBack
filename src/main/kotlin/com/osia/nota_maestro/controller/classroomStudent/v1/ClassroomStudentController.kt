package com.osia.nota_maestro.controller.classroomStudent.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentDto
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentMapper
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentRequest
import com.osia.nota_maestro.service.classroomStudent.ClassroomStudentService
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

@RestController("classroomStudent.v1.crud")
@CrossOrigin
@RequestMapping("v1/classroom-students")
@Validated
class ClassroomStudentController(
    private val classroomStudentService: ClassroomStudentService,
    private val classroomStudentMapper: ClassroomStudentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ClassroomStudentDto> {
        return classroomStudentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ClassroomStudentDto> {
        return classroomStudentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return classroomStudentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ClassroomStudentDto> {
        return ResponseEntity.ok().body(classroomStudentMapper.toDto(classroomStudentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ClassroomStudentDto>> {
        return ResponseEntity.ok().body(classroomStudentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ClassroomStudentRequest
    ): ResponseEntity<ClassroomStudentDto> {
        return ResponseEntity(classroomStudentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody classroomStudentRequestList: List<ClassroomStudentRequest>
    ): ResponseEntity<List<ClassroomStudentDto>> {
        return ResponseEntity(classroomStudentService.saveMultiple(classroomStudentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClassroomStudentRequest
    ): ResponseEntity<ClassroomStudentDto> {
        return ResponseEntity.ok().body(classroomStudentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody classroomStudentDtoList: List<ClassroomStudentDto>
    ): ResponseEntity<List<ClassroomStudentDto>> {
        return ResponseEntity.ok().body(classroomStudentService.updateMultiple(classroomStudentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        classroomStudentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        classroomStudentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
