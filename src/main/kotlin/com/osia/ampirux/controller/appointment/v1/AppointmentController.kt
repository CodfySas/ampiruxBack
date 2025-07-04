package com.osia.ampirux.controller.appointment.v1
import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.appointment.v1.AppointmentDto
import com.osia.ampirux.dto.appointment.v1.AppointmentMapper
import com.osia.ampirux.dto.appointment.v1.AppointmentRequest
import com.osia.ampirux.dto.appointment.v1.CalendarDto
import com.osia.ampirux.service.appointment.AppointmentService
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

@RestController("appointment.v1.crud")
@CrossOrigin
@RequestMapping("v1/appointments")
@Validated
class AppointmentController(
    private val service: AppointmentService,
    private val mapper: AppointmentMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page< AppointmentDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String,
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID): Page< AppointmentDto> {
        return service.findAllByFilter(pageable, where, barbershopUuid)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity< AppointmentDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List< AppointmentDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody request: AppointmentRequest): ResponseEntity<AppointmentDto > {
        request.barbershopUuid = barbershopUuid;
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody requestList: List< AppointmentRequest>): ResponseEntity<List<AppointmentDto > > {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: AppointmentRequest): ResponseEntity<AppointmentDto > {
        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List< AppointmentDto>): ResponseEntity<List<AppointmentDto > > {
        return ResponseEntity.ok(service.updateMultiple(dtoList))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        service.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(@RequestBody uuidList: List<UUID>): ResponseEntity<HttpStatus> {
        service.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/month/{month}/{year}")
    fun getByMonth(@PathVariable month: Int, @PathVariable year: Int, @RequestHeader("barbershop_uuid") barbershopUuid: UUID): ResponseEntity<List<List<CalendarDto>>> {
        return ResponseEntity(service.getCalendarMonth(year, month, barbershopUuid), HttpStatus.OK)
    }

    @GetMapping("/day/{day}/{month}/{year}")
    fun getByDay(
        @PathVariable day: Int,
        @PathVariable month: Int,
        @PathVariable year: Int,
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID
    ): ResponseEntity<CalendarDto> {
        return ResponseEntity(service.getCalendarDay(year, month, day, barbershopUuid), HttpStatus.OK)
    }
}
