package com.osia.ampirux.dto.client.v1

import com.osia.ampirux.dto.BaseDto
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class ClientDto : BaseDto() {
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var preferredBarberUuid: UUID? = null
    var dni: String? = null
    var notes: String? = null
    var barbershopUuid: UUID? = null
    var lastVisit: String? = null
    var visitCount: Int? = null
    var totalPay: BigDecimal? = null
    var lastPay: BigDecimal? = null
}
