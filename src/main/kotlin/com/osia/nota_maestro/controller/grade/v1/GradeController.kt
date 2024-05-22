package com.osia.nota_maestro.controller.grade.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.grade.v1.CourseInfoDto
import com.osia.nota_maestro.dto.grade.v1.GradeDto
import com.osia.nota_maestro.dto.grade.v1.GradeMapper
import com.osia.nota_maestro.dto.grade.v1.GradeRequest
import com.osia.nota_maestro.dto.grade.v1.GradeSubjectsDto
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.service.grade.GradeService
import com.osia.nota_maestro.service.log.LogService
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
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@RestController("grade.v1.crud")
@CrossOrigin
@RequestMapping("v1/grades")
@Validated
class GradeController(
    private val gradeService: GradeService,
    private val gradeMapper: GradeMapper,
    private val logService: LogService
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<GradeDto> {
        return gradeService.findAll(pageable, school)
    }

    @GetMapping("/complete-info")
    fun getCompleteInfo(@RequestHeader school: UUID): CourseInfoDto {
        return gradeService.findCompleteInfo(school)
    }

    @GetMapping("/subjects")
    fun getGradesWithSubjects(@RequestHeader school: UUID): List<GradeSubjectsDto> {
        return gradeService.getGradeWithSubjects(school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<GradeDto> {
        return gradeService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return gradeService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<GradeDto> {
        return ResponseEntity.ok().body(gradeMapper.toDto(gradeService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<GradeDto>> {
        return ResponseEntity.ok().body(gradeService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: GradeRequest
    ): ResponseEntity<GradeDto> {
        return ResponseEntity(gradeService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/complete")
    fun saveGrade(
        @Validated(OnCreate::class) @RequestBody request: CourseInfoDto,
        @RequestHeader school: UUID, @RequestHeader user: UUID?
    ): ResponseEntity<CourseInfoDto> {
        val time = LocalDateTime.now()
        val req1 = LogRequest().apply {
            this.day = LocalDate.now()
            this.hour = "${time.hour}:${time.second}"
            this.uuidUser = user
            this.movement = "ha actualizado los grados y los cursos"
        }
        val response = try {
            val res = gradeService.saveComplete(request, school)
            logService.save(req1.apply {
                this.status  = "Completado"
            })
            res
        } catch (e: Exception){
            logService.save(req1.apply {
                this.status  = "Error"
                this.detail = "${e.message}"
            })
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.message)
        }
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @PostMapping("/grade-subjects")
    fun saveGradeSubject(
        @Validated(OnCreate::class) @RequestBody request: List<GradeSubjectsDto>,
        @RequestHeader school: UUID
    ): ResponseEntity<List<GradeSubjectsDto>> {
        return ResponseEntity(gradeService.saveGradeSubjects(request, school), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody gradeRequestList: List<GradeRequest>
    ): ResponseEntity<List<GradeDto>> {
        return ResponseEntity(gradeService.saveMultiple(gradeRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: GradeRequest
    ): ResponseEntity<GradeDto> {
        return ResponseEntity.ok().body(gradeService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody gradeDtoList: List<GradeDto>
    ): ResponseEntity<List<GradeDto>> {
        return ResponseEntity.ok().body(gradeService.updateMultiple(gradeDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        gradeService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        gradeService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
