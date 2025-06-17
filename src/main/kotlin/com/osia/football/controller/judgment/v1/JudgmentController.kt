package com.osia.nota_maestro.controller.judgment.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.judgment.v1.JudgmentDto
import com.osia.nota_maestro.dto.judgment.v1.JudgmentMapper
import com.osia.nota_maestro.dto.judgment.v1.JudgmentRequest
import com.osia.nota_maestro.service.judgment.JudgmentService
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

@RestController("judgment.v1.crud")
@CrossOrigin
@RequestMapping("v1/judgments")
@Validated
class JudgmentController(
    private val judgmentService: JudgmentService,
    private val judgmentMapper: JudgmentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<JudgmentDto> {
        return judgmentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<JudgmentDto> {
        return judgmentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return judgmentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<JudgmentDto> {
        return ResponseEntity.ok().body(judgmentMapper.toDto(judgmentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<JudgmentDto>> {
        return ResponseEntity.ok().body(judgmentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: JudgmentRequest
    ): ResponseEntity<JudgmentDto> {
        return ResponseEntity(judgmentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody judgmentRequestList: List<JudgmentRequest>
    ): ResponseEntity<List<JudgmentDto>> {
        return ResponseEntity(judgmentService.saveMultiple(judgmentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: JudgmentRequest
    ): ResponseEntity<JudgmentDto> {
        return ResponseEntity.ok().body(judgmentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody judgmentDtoList: List<JudgmentDto>
    ): ResponseEntity<List<JudgmentDto>> {
        return ResponseEntity.ok().body(judgmentService.updateMultiple(judgmentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        judgmentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        judgmentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
