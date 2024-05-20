package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.mesh.MeshListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "meshs",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        MeshListener::class
    ]
)
@Where(clause = "deleted = false")
data class Mesh(
    var axis: String? = null,
    var content: String? = null,
    var achievements: String? = null,
    var achievementIndicator: String? = null,
    var strategies: String? = null,
    var skills: String? = null,
    var classroom: UUID? = null,
    var subject: UUID? = null,
    var period: Int? = null,
    var observation: String? = "",
    var status: String? = "pending",
    var userReview: UUID? = null,
    var position: Int? = null,
    var uuidTeacher: UUID? = null
) : BaseModel()
