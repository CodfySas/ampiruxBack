package com.osia.nota_maestro.controller.accompanimentStudent.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentMapper
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentRequest
import com.osia.nota_maestro.service.accompanimentStudent.AccompanimentStudentService
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

@RestController("accompanimentStudent.v1.crud")
@CrossOrigin
@RequestMapping("v1/accompaniment-students")
@Validated
class AccompanimentStudentController(
    private val accompanimentStudentService: AccompanimentStudentService,
    private val accompanimentStudentMapper: AccompanimentStudentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<AccompanimentStudentDto> {
        return accompanimentStudentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<AccompanimentStudentDto> {
        return accompanimentStudentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return accompanimentStudentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<AccompanimentStudentDto> {
        return ResponseEntity.ok().body(accompanimentStudentMapper.toDto(accompanimentStudentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<AccompanimentStudentDto>> {
        return ResponseEntity.ok().body(accompanimentStudentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: AccompanimentStudentRequest
    ): ResponseEntity<AccompanimentStudentDto> {
        return ResponseEntity(accompanimentStudentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody accompanimentStudentRequestList: List<AccompanimentStudentRequest>
    ): ResponseEntity<List<AccompanimentStudentDto>> {
        return ResponseEntity(accompanimentStudentService.saveMultiple(accompanimentStudentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: AccompanimentStudentRequest
    ): ResponseEntity<AccompanimentStudentDto> {
        return ResponseEntity.ok().body(accompanimentStudentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody accompanimentStudentDtoList: List<AccompanimentStudentDto>
    ): ResponseEntity<List<AccompanimentStudentDto>> {
        return ResponseEntity.ok().body(accompanimentStudentService.updateMultiple(accompanimentStudentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        accompanimentStudentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        accompanimentStudentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
