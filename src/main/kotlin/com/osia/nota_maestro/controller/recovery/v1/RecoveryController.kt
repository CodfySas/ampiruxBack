package com.osia.nota_maestro.controller.recovery.v1

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.service.recovery.RecoveryService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("recovery.v1.crud")
@CrossOrigin
@RequestMapping("v1/recoveries")
@Validated
class RecoveryController(
    private val recoveryService: RecoveryService,
) {
    @GetMapping("/{teacher}")
    fun getById(@PathVariable teacher: UUID): ResponseEntity<RecoveryDto> {
        return ResponseEntity.ok().body(recoveryService.getMyNotes(teacher))
    }

    @PostMapping("/submit/{teacher}")
    fun submitNotes(@PathVariable teacher: UUID, @RequestBody notes: RecoveryDto): ResponseEntity<RecoveryDto> {
        return ResponseEntity.ok().body(recoveryService.submitNotes(notes, teacher))
    }
}
