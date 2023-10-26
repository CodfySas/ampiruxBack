package com.osia.nota_maestro.controller.calendar.v1

import com.osia.nota_maestro.dto.calendar.v1.CalendarDto
import com.osia.nota_maestro.service.calendarTask.CalendarTaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("calendar.v1.crud")
@CrossOrigin
@RequestMapping("v1/calendar")
@Validated
class CalendarController(
    private val calendarTaskService: CalendarTaskService
) {
    @GetMapping("/month/{month}/{year}")
    fun getByMonth(@PathVariable month: Int, @PathVariable year: Int): ResponseEntity<List<List<CalendarDto>>> {
        return ResponseEntity(calendarTaskService.getCalendarMonth(year, month), HttpStatus.OK)
    }

    @GetMapping("/day/{day}/{month}/{year}")
    fun getByDay(
        @PathVariable day: Int,
        @PathVariable month: Int,
        @PathVariable year: Int
    ): ResponseEntity<com.osia.nota_maestro.dto.calendar.v1.CalendarDto> {
        return ResponseEntity(calendarTaskService.getCalendarDay(year, month, day), HttpStatus.OK)
    }
}
