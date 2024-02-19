package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.directorStudent.DirectorStudentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "director_students",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        DirectorStudentListener::class
    ]
)
@Where(clause = "deleted = false")
data class DirectorStudent(
    var uuidClassroomStudent: UUID? = null,
    var uuidStudent: UUID? = null,
    var period: Int? = null,
    var description: String? = null
) : BaseModel()
