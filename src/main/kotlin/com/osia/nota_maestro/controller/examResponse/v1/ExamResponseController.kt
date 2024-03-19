package com.osia.nota_maestro.controller.examResponse.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseMapper
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseRequest
import com.osia.nota_maestro.service.examResponse.ExamResponseService
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

@RestController("examResponse.v1.crud")
@CrossOrigin
@RequestMapping("v1/exam-responses")
@Validated
class ExamResponseController(
    private val examResponseService: ExamResponseService,
    private val examResponseMapper: ExamResponseMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ExamResponseDto> {
        return examResponseService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ExamResponseDto> {
        return examResponseService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return examResponseService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ExamResponseDto> {
        return ResponseEntity.ok().body(examResponseMapper.toDto(examResponseService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ExamResponseDto>> {
        return ResponseEntity.ok().body(examResponseService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ExamResponseRequest
    ): ResponseEntity<ExamResponseDto> {
        return ResponseEntity(examResponseService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody examResponseRequestList: List<ExamResponseRequest>
    ): ResponseEntity<List<ExamResponseDto>> {
        return ResponseEntity(examResponseService.saveMultiple(examResponseRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ExamResponseRequest
    ): ResponseEntity<ExamResponseDto> {
        return ResponseEntity.ok().body(examResponseService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody examResponseDtoList: List<ExamResponseDto>
    ): ResponseEntity<List<ExamResponseDto>> {
        return ResponseEntity.ok().body(examResponseService.updateMultiple(examResponseDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        examResponseService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        examResponseService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
