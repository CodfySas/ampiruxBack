package com.osia.nota_maestro.controller.schoolPeriod.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodDto
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodMapper
import com.osia.nota_maestro.dto.schoolPeriod.v1.SchoolPeriodRequest
import com.osia.nota_maestro.service.schoolPeriod.SchoolPeriodService
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

@RestController("schoolPeriod.v1.crud")
@CrossOrigin
@RequestMapping("v1/school-periods")
@Validated
class SchoolPeriodController(
    private val schoolPeriodService: SchoolPeriodService,
    private val schoolPeriodMapper: SchoolPeriodMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<SchoolPeriodDto> {
        return schoolPeriodService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<SchoolPeriodDto> {
        return schoolPeriodService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return schoolPeriodService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<SchoolPeriodDto> {
        return ResponseEntity.ok().body(schoolPeriodMapper.toDto(schoolPeriodService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<SchoolPeriodDto>> {
        return ResponseEntity.ok().body(schoolPeriodService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: SchoolPeriodRequest
    ): ResponseEntity<SchoolPeriodDto> {
        return ResponseEntity(schoolPeriodService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody schoolPeriodRequestList: List<SchoolPeriodRequest>
    ): ResponseEntity<List<SchoolPeriodDto>> {
        return ResponseEntity(schoolPeriodService.saveMultiple(schoolPeriodRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: SchoolPeriodRequest
    ): ResponseEntity<SchoolPeriodDto> {
        return ResponseEntity.ok().body(schoolPeriodService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody schoolPeriodDtoList: List<SchoolPeriodDto>
    ): ResponseEntity<List<SchoolPeriodDto>> {
        return ResponseEntity.ok().body(schoolPeriodService.updateMultiple(schoolPeriodDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        schoolPeriodService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        schoolPeriodService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
