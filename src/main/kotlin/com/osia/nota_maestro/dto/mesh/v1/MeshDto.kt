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
}
