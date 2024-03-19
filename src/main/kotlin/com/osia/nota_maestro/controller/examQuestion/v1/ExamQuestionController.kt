package com.osia.nota_maestro.controller.examQuestion.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionMapper
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionRequest
import com.osia.nota_maestro.service.examQuestion.ExamQuestionService
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

@RestController("examQuestion.v1.crud")
@CrossOrigin
@RequestMapping("v1/exam-questions")
@Validated
class ExamQuestionController(
    private val examQuestionService: ExamQuestionService,
    private val examQuestionMapper: ExamQuestionMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ExamQuestionDto> {
        return examQuestionService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ExamQuestionDto> {
        return examQuestionService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return examQuestionService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ExamQuestionDto> {
        return ResponseEntity.ok().body(examQuestionMapper.toDto(examQuestionService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ExamQuestionDto>> {
        return ResponseEntity.ok().body(examQuestionService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ExamQuestionRequest
    ): ResponseEntity<ExamQuestionDto> {
        return ResponseEntity(examQuestionService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody examQuestionRequestList: List<ExamQuestionRequest>
    ): ResponseEntity<List<ExamQuestionDto>> {
        return ResponseEntity(examQuestionService.saveMultiple(examQuestionRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ExamQuestionRequest
    ): ResponseEntity<ExamQuestionDto> {
        return ResponseEntity.ok().body(examQuestionService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody examQuestionDtoList: List<ExamQuestionDto>
    ): ResponseEntity<List<ExamQuestionDto>> {
        return ResponseEntity.ok().body(examQuestionService.updateMultiple(examQuestionDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        examQuestionService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        examQuestionService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
