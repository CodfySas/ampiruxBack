package com.osia.nota_maestro.dto.subModuleUser.v1

import java.util.UUID

class SubModuleUserRequest {
    var uuidUser: UUID? = null
    var uuidSubModules: List<UUID> = mutableListOf()
}
