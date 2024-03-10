package com.osia.nota_maestro.dto.mesh.v1

import com.osia.nota_maestro.dto.BaseDto
import java.util.UUID

class MeshDto : BaseDto() {
    var axis: String? = null
    var content: String? = null
    var achievements: String? = null
    var achievementIndicator: String? = null
    var strategies: String? = null
    var skills: String? = null
    var classroom: UUID? = null
    var subject: UUID? = null
    var period: Int? = null
    var observation: String? = null
    var status: String? = null
    var userReview: UUID? = null
    var position: Int? = null
}
