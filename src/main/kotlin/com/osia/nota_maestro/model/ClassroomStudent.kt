package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.classroomStudent.ClassroomStudentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "classroom_students",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClassroomStudentListener::class
    ]
)
@Where(clause = "deleted = false")
data class ClassroomStudent(
    var uuidStudent: UUID? = null,
    var uuidClassroom: UUID? = null
) : BaseModel()
