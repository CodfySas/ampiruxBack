package com.osia.nota_maestro.controller.diagnosis.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisCompleteDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisMapper
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisRequest
import com.osia.nota_maestro.service.diagnosis.DiagnosisService
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

@RestController("diagnosis.v1.crud")
@CrossOrigin
@RequestMapping("v1/diagnoses")
@Validated
class DiagnosisController(
    private val diagnosisService: DiagnosisService,
    private val diagnosisMapper: DiagnosisMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<DiagnosisDto> {
        return diagnosisService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<DiagnosisDto> {
        return diagnosisService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return diagnosisService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<DiagnosisDto> {
        return ResponseEntity.ok().body(diagnosisMapper.toDto(diagnosisService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<DiagnosisDto>> {
        return ResponseEntity.ok().body(diagnosisService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: DiagnosisRequest
    ): ResponseEntity<DiagnosisDto> {
        return ResponseEntity(diagnosisService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody diagnosisRequestList: List<DiagnosisRequest>
    ): ResponseEntity<List<DiagnosisDto>> {
        return ResponseEntity(diagnosisService.saveMultiple(diagnosisRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: DiagnosisRequest
    ): ResponseEntity<DiagnosisDto> {
        return ResponseEntity.ok().body(diagnosisService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody diagnosisDtoList: List<DiagnosisDto>
    ): ResponseEntity<List<DiagnosisDto>> {
        return ResponseEntity.ok().body(diagnosisService.updateMultiple(diagnosisDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        diagnosisService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        diagnosisService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/complete")
    fun getComplete(@RequestHeader school: UUID): ResponseEntity<DiagnosisCompleteDto> {
        return ResponseEntity.ok().body(diagnosisService.getComplete(school))
    }

    @PostMapping("/complete")
    fun submitComplete(@RequestHeader school: UUID, @RequestBody req: List<DiagnosisDto>): ResponseEntity<DiagnosisCompleteDto> {
        return ResponseEntity.ok().body(diagnosisService.submitComplete(req, school))
    }
}
