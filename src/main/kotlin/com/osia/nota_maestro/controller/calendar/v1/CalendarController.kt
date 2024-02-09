package com.osia.nota_maestro.controller.calendar.v1

import com.osia.nota_maestro.dto.calendar.v1.CalendarDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskDto
import com.osia.nota_maestro.dto.calendar.v1.CalendarTaskRequest
import com.osia.nota_maestro.service.calendarTask.CalendarTaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("calendar.v1.crud")
@CrossOrigin
@RequestMapping("v1/resources")
@Validated
class CalendarController(
    private val calendarTaskService: CalendarTaskService
) {
    @GetMapping("/month/{month}/{year}")
    fun getByMonth(@PathVariable month: Int, @PathVariable year: Int, @RequestHeader school: UUID): ResponseEntity<List<List<CalendarDto>>> {
        return ResponseEntity(calendarTaskService.getCalendarMonth(year, month, school), HttpStatus.OK)
    }

    @GetMapping("/day/{day}/{month}/{year}")
    fun getByDay(
        @PathVariable day: Int,
        @PathVariable month: Int,
        @PathVariable year: Int,
        @RequestHeader school: UUID
    ): ResponseEntity<CalendarDto> {
        return ResponseEntity(calendarTaskService.getCalendarDay(year, month, day, school), HttpStatus.OK)
    }

    @PostMapping("/task")
    fun submitTask(@RequestHeader school: UUID, @RequestBody calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto {
        return calendarTaskService.submitTask(school, calendarTaskRequest)
    }

    @PatchMapping("/task/{uuid}")
    fun updateTask(@RequestHeader school: UUID, @PathVariable uuid: UUID, @RequestBody calendarTaskRequest: CalendarTaskRequest): CalendarTaskDto {
        return calendarTaskService.updateTask(school, uuid, calendarTaskRequest)
    }
}
