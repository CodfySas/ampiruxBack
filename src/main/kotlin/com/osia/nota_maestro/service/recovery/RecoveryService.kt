package com.osia.nota_maestro.service.recovery

import com.osia.nota_maestro.dto.recovery.v1.RecoveryDto
import com.osia.nota_maestro.dto.resources.v1.ResourceRequest

interface RecoveryService {
    fun getMyRecovery(request: ResourceRequest): RecoveryDto
    fun submitRecovery(recoveriesDto: List<RecoveryDto>): List<RecoveryDto>
}
