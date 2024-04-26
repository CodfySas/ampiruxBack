package com.osia.nota_maestro.controller.attendance.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.attendance.v1.AttendanceCompleteDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceMapper
import com.osia.nota_maestro.dto.attendance.v1.AttendanceRequest
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceStudentDto
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.dto.resources.v1.ResourceSubjectDto
import com.osia.nota_maestro.service.attendance.AttendanceService
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

@RestController("attendance.v1.crud")
@CrossOrigin
@RequestMapping("v1/attendances")
@Validated
class AttendanceController(
    private val attendanceService: AttendanceService,
    private val attendanceMapper: AttendanceMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<AttendanceDto> {
        return attendanceService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<AttendanceDto> {
        return attendanceService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return attendanceService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<AttendanceDto> {
        return ResponseEntity.ok().body(attendanceMapper.toDto(attendanceService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<AttendanceDto>> {
        return ResponseEntity.ok().body(attendanceService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: AttendanceRequest
    ): ResponseEntity<AttendanceDto> {
        return ResponseEntity(attendanceService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody attendanceRequestList: List<AttendanceRequest>
    ): ResponseEntity<List<AttendanceDto>> {
        return ResponseEntity(attendanceService.saveMultiple(attendanceRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: AttendanceRequest
    ): ResponseEntity<AttendanceDto> {
        return ResponseEntity.ok().body(attendanceService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody attendanceDtoList: List<AttendanceDto>
    ): ResponseEntity<List<AttendanceDto>> {
        return ResponseEntity.ok().body(attendanceService.updateMultiple(attendanceDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        attendanceService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        attendanceService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/complete/{classroom}/{subject}/{month}")
    fun getComplete(
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable month: Int,
        @RequestHeader school: UUID
    ): ResponseEntity<List<AttendanceCompleteDto>> {
        return ResponseEntity.ok().body(attendanceService.getComplete(classroom, subject, month, school))
    }

    @GetMapping("/complete-group/{classroom}/{month}")
    fun getCompleteGroup(
        @PathVariable classroom: UUID,
        @PathVariable month: Int,
        @RequestHeader school: UUID
    ): ResponseEntity<List<AttendanceCompleteDto>> {
        return ResponseEntity.ok().body(attendanceService.getCompleteGroup(classroom, month, school))
    }

    @GetMapping("/complete-student/{uuid}/{subject}/{month}")
    fun getByStudent(
        @PathVariable uuid: UUID,
        @PathVariable subject: UUID,
        @PathVariable month: Int
    ): ResponseEntity<List<List<AttendanceStudentDto>>> {
        return ResponseEntity.ok().body(attendanceService.getByStudent(uuid, subject, month))
    }

    @PostMapping("/complete/{classroom}/{subject}/{month}")
    fun submit(
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable month: Int,
        @RequestHeader school: UUID,
        @RequestBody req: List<AttendanceCompleteDto>
    ): ResponseEntity<List<AttendanceCompleteDto>> {
        return ResponseEntity.ok().body(attendanceService.submit(classroom, subject, month, school, req))
    }

    @PostMapping("/complete-group/{classroom}/{month}")
    fun submitGrouped(
        @PathVariable classroom: UUID,
        @PathVariable month: Int,
        @RequestHeader school: UUID,
        @RequestBody req: List<AttendanceCompleteDto>
    ): ResponseEntity<List<AttendanceCompleteDto>> {
        return ResponseEntity.ok().body(attendanceService.submitGroup(classroom, month, school, req))
    }

    @GetMapping("/resources/{uuid}")
    fun getResources(@PathVariable uuid: UUID): List<ResourceGradeDto> {
        return attendanceService.getResources(uuid)
    }

    @GetMapping("/resources-student/{uuid}")
    fun getResourcesStudent(@PathVariable uuid: UUID): List<ResourceSubjectDto> {
        return attendanceService.getResourcesStudent(uuid)
    }
}
