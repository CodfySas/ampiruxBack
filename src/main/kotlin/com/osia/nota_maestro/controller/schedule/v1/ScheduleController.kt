package com.osia.nota_maestro.controller.schedule.v1

/*import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.schedule.v1.ScheduleDto
import com.osia.nota_maestro.dto.schedule.v1.ScheduleMapper
import com.osia.nota_maestro.dto.schedule.v1.ScheduleRequest
import com.osia.nota_maestro.service.schedule.ScheduleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity*/
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

@RestController("schedule.v1.crud")
@CrossOrigin
@RequestMapping("v1/schedules")
@Validated
class ScheduleController(/*
    private val scheduleService: ScheduleService,
    private val scheduleMapper: ScheduleMapper*/
) {
    // Read
   /* @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ScheduleDto> {
        return scheduleService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<ScheduleDto> {
        return scheduleService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return scheduleService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ScheduleDto> {
        return ResponseEntity.ok().body(scheduleMapper.toDto(scheduleService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ScheduleDto>> {
        return ResponseEntity.ok().body(scheduleService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleDto> {
        return ResponseEntity(scheduleService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody scheduleRequestList: List<ScheduleRequest>
    ): ResponseEntity<List<ScheduleDto>> {
        return ResponseEntity(scheduleService.saveMultiple(scheduleRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ScheduleRequest
    ): ResponseEntity<ScheduleDto> {
        return ResponseEntity.ok().body(scheduleService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody scheduleDtoList: List<ScheduleDto>
    ): ResponseEntity<List<ScheduleDto>> {
        return ResponseEntity.ok().body(scheduleService.updateMultiple(scheduleDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        scheduleService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        scheduleService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }*/
}
