package com.osia.nota_maestro.dto.mesh.v1

import java.util.UUID

class MeshRequest {
    var axis: String? = null
    var content: String? = null
    var achievements: String? = null
    var achievementIndicator: String? = null
    var strategies: String? = null
    var skills: String? = null
    var classroom: UUID? = null
    var subject: UUID? = null
    var period: Int? = null
}
