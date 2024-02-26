package com.osia.nota_maestro.controller.attendanceFail.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailDto
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailMapper
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailRequest
import com.osia.nota_maestro.service.attendanceFail.AttendanceFailService
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

@RestController("attendanceFail.v1.crud")
@CrossOrigin
@RequestMapping("v1/attendance-fails")
@Validated
class AttendanceFailController(
    private val attendanceFailService: AttendanceFailService,
    private val attendanceFailMapper: AttendanceFailMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<AttendanceFailDto> {
        return attendanceFailService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<AttendanceFailDto> {
        return attendanceFailService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return attendanceFailService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<AttendanceFailDto> {
        return ResponseEntity.ok().body(attendanceFailMapper.toDto(attendanceFailService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<AttendanceFailDto>> {
        return ResponseEntity.ok().body(attendanceFailService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: AttendanceFailRequest
    ): ResponseEntity<AttendanceFailDto> {
        return ResponseEntity(attendanceFailService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody attendanceFailRequestList: List<AttendanceFailRequest>
    ): ResponseEntity<List<AttendanceFailDto>> {
        return ResponseEntity(attendanceFailService.saveMultiple(attendanceFailRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: AttendanceFailRequest
    ): ResponseEntity<AttendanceFailDto> {
        return ResponseEntity.ok().body(attendanceFailService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody attendanceFailDtoList: List<AttendanceFailDto>
    ): ResponseEntity<List<AttendanceFailDto>> {
        return ResponseEntity.ok().body(attendanceFailService.updateMultiple(attendanceFailDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        attendanceFailService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        attendanceFailService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
