package com.osia.ampirux.dto.auxi.v1

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class ClientVisitStatsDto(
    val clientUuid: UUID?,
    val lastVisit: LocalDateTime?,
    val visitCount: Int?,
    val totalPay: BigDecimal?,
    val lastPay: BigDecimal?
)