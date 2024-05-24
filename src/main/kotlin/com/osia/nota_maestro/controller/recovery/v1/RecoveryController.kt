package com.osia.nota_maestro.controller.recovery.v1

import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.service.log.LogService
import com.osia.nota_maestro.service.recovery.RecoveryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@RestController("recovery.v1.crud")
@CrossOrigin
@RequestMapping("v1/recoveries")
@Validated
class RecoveryController(
    private val recoveryService: RecoveryService,
    private val logService: LogService
) {
    @PostMapping
    fun getById(@RequestBody request: ResourceRequest): ResponseEntity<RecoveryDto> {
        return ResponseEntity.ok().body(recoveryService.getMyRecovery(request))
    }

    @PostMapping("/submit")
    fun submitNotes(@RequestBody notes: List<RecoveryDto>, @RequestHeader school: UUID, @RequestHeader user: UUID?): ResponseEntity<List<RecoveryDto>> {
        val time = LocalDateTime.now(ZoneId.of("America/Bogota"))
        val req1 = LogRequest().apply {
            this.day = LocalDate.now(ZoneId.of("America/Bogota"))
            this.uuidSchool = school
            this.hour = "${String.format("%02d", time.hour)}:${String.format("%02d", time.minute)}:${String.format("%02d", time.second)}"
            this.uuidUser = user
            this.movement = "ha actualizado las asistencias"
        }
        val response = try {
            val res = recoveryService.submitRecovery(notes)
            logService.save(
                req1.apply {
                    this.status = "Completado"
                }
            )
            res
        } catch (e: Exception) {
            logService.save(
                req1.apply {
                    this.status = "Error"
                    this.detail = "${e.message}"
                }
            )
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.message)
        }
        return ResponseEntity.ok().body(response)
    }
}
