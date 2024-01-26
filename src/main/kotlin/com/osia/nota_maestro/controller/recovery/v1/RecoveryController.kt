package com.osia.nota_maestro.controller.recovery.v1

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest
import com.osia.nota_maestro.service.recovery.RecoveryService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("recovery.v1.crud")
@CrossOrigin
@RequestMapping("v1/recoveries")
@Validated
class RecoveryController(
    private val recoveryService: RecoveryService,
) {
    @PostMapping
    fun getById(@RequestBody request: ResourceRequest): ResponseEntity<RecoveryDto> {
        return ResponseEntity.ok().body(recoveryService.getMyRecovery(request))
    }

    @PostMapping("/submit")
    fun submitNotes(@RequestBody notes: List<RecoveryDto>): ResponseEntity<List<RecoveryDto>> {
        return ResponseEntity.ok().body(recoveryService.submitRecovery(notes))
    }
}
