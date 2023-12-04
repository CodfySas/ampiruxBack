package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.classroom.ClassroomListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "classrooms",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClassroomListener::class
    ]
)
@Where(clause = "deleted = false")
data class Classroom(
    var name: String? = null,
    var year: Int? = null,
    var uuidGrade: UUID? = null,
    @NotNull
    var uuidSchool: UUID? = null
) : BaseModel()
