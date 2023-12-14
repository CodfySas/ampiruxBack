package com.osia.nota_maestro.controller.classroomSubject.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectCompleteDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectMapper
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectRequest
import com.osia.nota_maestro.dto.classroomSubject.v1.CompleteSubjectsTeachersDto
import com.osia.nota_maestro.service.classroomSubject.ClassroomSubjectService
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

@RestController("classroomSubject.v1.crud")
@CrossOrigin
@RequestMapping("v1/classroom-subjects")
@Validated
class ClassroomSubjectController(
    private val classroomSubjectService: ClassroomSubjectService,
    private val classroomSubjectMapper: ClassroomSubjectMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ClassroomSubjectDto> {
        return classroomSubjectService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ClassroomSubjectDto> {
        return classroomSubjectService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return classroomSubjectService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ClassroomSubjectDto> {
        return ResponseEntity.ok().body(classroomSubjectMapper.toDto(classroomSubjectService.getById(uuid)))
    }

    @GetMapping("/complete-info")
    fun getGradesAndClassroomSubjects(@RequestHeader school: UUID): ResponseEntity<List<ClassroomSubjectCompleteDto>> {
        return ResponseEntity.ok().body(classroomSubjectService.getCompleteInfo(school))
    }

    @GetMapping("/complete-info-2")
    fun getGradesAndClassroomSubjects2(@RequestHeader school: UUID): ResponseEntity<CompleteSubjectsTeachersDto> {
        return ResponseEntity.ok().body(classroomSubjectService.getCompleteInfo2(school))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ClassroomSubjectDto>> {
        return ResponseEntity.ok().body(classroomSubjectService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ClassroomSubjectRequest
    ): ResponseEntity<ClassroomSubjectDto> {
        return ResponseEntity(classroomSubjectService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody classroomSubjectRequestList: List<ClassroomSubjectRequest>
    ): ResponseEntity<List<ClassroomSubjectDto>> {
        return ResponseEntity(classroomSubjectService.saveMultiple(classroomSubjectRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClassroomSubjectRequest
    ): ResponseEntity<ClassroomSubjectDto> {
        return ResponseEntity.ok().body(classroomSubjectService.update(uuid, request,))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody classroomSubjectDtoList: List<ClassroomSubjectDto>
    ): ResponseEntity<List<ClassroomSubjectDto>> {
        return ResponseEntity.ok().body(classroomSubjectService.updateMultiple(classroomSubjectDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        classroomSubjectService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        classroomSubjectService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
