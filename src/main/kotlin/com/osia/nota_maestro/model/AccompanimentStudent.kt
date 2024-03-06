package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.accompanimentStudent.AccompanimentStudentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "accompaniment_students",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AccompanimentStudentListener::class
    ]
)
@Where(clause = "deleted = false")
data class AccompanimentStudent(
    var uuidClassroomStudent: UUID? = null,
    var uuidStudent: UUID? = null,
    var period: Int? = null,
    var description: String? = null
) : BaseModel()
