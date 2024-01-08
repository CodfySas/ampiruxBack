package com.osia.nota_maestro.service.recovery

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import java.util.UUID

interface RecoveryService {
    fun getMyRecovery(teacher: UUID): RecoveryDto
    fun submitRecovery(recoveryDto: RecoveryDto, teacher: UUID): RecoveryDto
}
