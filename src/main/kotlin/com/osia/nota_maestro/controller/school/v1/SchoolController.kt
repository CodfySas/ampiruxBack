package com.osia.nota_maestro.controller.school.v1

import com.osia.nota_maestro.dto.school.v1.SchoolDto
import com.osia.nota_maestro.dto.school.v1.SchoolMapper
import com.osia.nota_maestro.dto.school.v1.SchoolMinDto
import com.osia.nota_maestro.dto.school.v1.SchoolRequest
import com.osia.nota_maestro.service.classroom.ClassroomService
import com.osia.nota_maestro.service.classroomStudent.ClassroomStudentService
import com.osia.nota_maestro.service.grade.GradeService
import com.osia.nota_maestro.service.school.SchoolService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("school.v1.crud")
@CrossOrigin
@RequestMapping("v1/schools")
@Validated
class SchoolController(
    private val schoolService: SchoolService,
    private val gradeService: GradeService,
    private val classroomStudentService: ClassroomStudentService,
    private val classroomService: ClassroomService,
    private val schoolMapper: SchoolMapper
) {
    @PatchMapping("/update")
    fun update(
        @RequestHeader school: UUID,
        @RequestBody request: SchoolRequest
    ): ResponseEntity<SchoolDto> {
        return ResponseEntity.ok().body(schoolService.update(school, request))
    }

    @GetMapping
    fun getSchool(@RequestHeader school: UUID): ResponseEntity<SchoolMinDto> {
        return ResponseEntity.ok().body(schoolMapper.toMin(schoolService.getById(school)))
    }
}
