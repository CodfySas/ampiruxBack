package com.osia.ampirux.dto.client.v1

import java.util.UUID

class ClientRequest {
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var preferredBarberUuid: UUID? = null
    var dni: String? = null
    var notes: String? = null
    var barbershopUuid: UUID? = null
}
