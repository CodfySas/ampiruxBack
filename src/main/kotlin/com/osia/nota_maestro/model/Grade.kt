package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.grade.GradeListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "grades",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        GradeListener::class
    ]
)
@Where(clause = "deleted = false")
data class Grade(
    var name: String? = null,
    @NotNull
    var uuidSchool: UUID? = null
) : BaseModel()
