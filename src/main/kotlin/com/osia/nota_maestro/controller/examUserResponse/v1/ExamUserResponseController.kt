package com.osia.nota_maestro.controller.examUserResponse.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseDto
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseMapper
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseRequest
import com.osia.nota_maestro.service.examUserResponse.ExamUserResponseService
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

@RestController("examUserResponse.v1.crud")
@CrossOrigin
@RequestMapping("v1/exam-user-responses")
@Validated
class ExamUserResponseController(
    private val examUserResponseService: ExamUserResponseService,
    private val examUserResponseMapper: ExamUserResponseMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ExamUserResponseDto> {
        return examUserResponseService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ExamUserResponseDto> {
        return examUserResponseService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return examUserResponseService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ExamUserResponseDto> {
        return ResponseEntity.ok().body(examUserResponseMapper.toDto(examUserResponseService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ExamUserResponseDto>> {
        return ResponseEntity.ok().body(examUserResponseService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ExamUserResponseRequest
    ): ResponseEntity<ExamUserResponseDto> {
        return ResponseEntity(examUserResponseService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody examUserResponseRequestList: List<ExamUserResponseRequest>
    ): ResponseEntity<List<ExamUserResponseDto>> {
        return ResponseEntity(examUserResponseService.saveMultiple(examUserResponseRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ExamUserResponseRequest
    ): ResponseEntity<ExamUserResponseDto> {
        return ResponseEntity.ok().body(examUserResponseService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody examUserResponseDtoList: List<ExamUserResponseDto>
    ): ResponseEntity<List<ExamUserResponseDto>> {
        return ResponseEntity.ok().body(examUserResponseService.updateMultiple(examUserResponseDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        examUserResponseService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        examUserResponseService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
