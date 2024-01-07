package com.osia.nota_maestro.service.recovery

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import java.util.UUID

interface RecoveryService {
    fun getMyNotes(teacher: UUID): RecoveryDto
    fun submitNotes(noteDto: RecoveryDto, teacher: UUID): RecoveryDto
}
