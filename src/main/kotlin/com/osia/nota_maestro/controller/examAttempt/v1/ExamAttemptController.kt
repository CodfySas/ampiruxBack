package com.osia.nota_maestro.controller.examAttempt.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptDto
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptMapper
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptRequest
import com.osia.nota_maestro.service.examAttempt.ExamAttemptService
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

@RestController("examAttempt.v1.crud")
@CrossOrigin
@RequestMapping("v1/exam-attempts")
@Validated
class ExamAttemptController(
    private val examAttemptService: ExamAttemptService,
    private val examAttemptMapper: ExamAttemptMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ExamAttemptDto> {
        return examAttemptService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ExamAttemptDto> {
        return examAttemptService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return examAttemptService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ExamAttemptDto> {
        return ResponseEntity.ok().body(examAttemptMapper.toDto(examAttemptService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ExamAttemptDto>> {
        return ResponseEntity.ok().body(examAttemptService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ExamAttemptRequest
    ): ResponseEntity<ExamAttemptDto> {
        return ResponseEntity(examAttemptService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody examAttemptRequestList: List<ExamAttemptRequest>
    ): ResponseEntity<List<ExamAttemptDto>> {
        return ResponseEntity(examAttemptService.saveMultiple(examAttemptRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ExamAttemptRequest
    ): ResponseEntity<ExamAttemptDto> {
        return ResponseEntity.ok().body(examAttemptService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody examAttemptDtoList: List<ExamAttemptDto>
    ): ResponseEntity<List<ExamAttemptDto>> {
        return ResponseEntity.ok().body(examAttemptService.updateMultiple(examAttemptDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        examAttemptService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        examAttemptService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
