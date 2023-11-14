package com.osia.nota_maestro.controller.teacher.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.teacher.v1.TeacherDto
import com.osia.nota_maestro.dto.teacher.v1.TeacherMapper
import com.osia.nota_maestro.dto.teacher.v1.TeacherRequest
import com.osia.nota_maestro.service.teacher.TeacherService
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

@RestController("teacher.v1.crud")
@CrossOrigin
@RequestMapping("v1/teachers")
@Validated
class TeacherController(
    private val teacherService: TeacherService,
    private val teacherMapper: TeacherMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<TeacherDto> {
        return teacherService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<TeacherDto> {
        return teacherService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return teacherService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<TeacherDto> {
        return ResponseEntity.ok().body(teacherMapper.toDto(teacherService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<TeacherDto>> {
        return ResponseEntity.ok().body(teacherService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: TeacherRequest
    ): ResponseEntity<TeacherDto> {
        return ResponseEntity(teacherService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody teacherRequestList: List<TeacherRequest>
    ): ResponseEntity<List<TeacherDto>> {
        return ResponseEntity(teacherService.saveMultiple(teacherRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: TeacherRequest
    ): ResponseEntity<TeacherDto> {
        return ResponseEntity.ok().body(teacherService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody teacherDtoList: List<TeacherDto>
    ): ResponseEntity<List<TeacherDto>> {
        return ResponseEntity.ok().body(teacherService.updateMultiple(teacherDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        teacherService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        teacherService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
