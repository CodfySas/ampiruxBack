package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.studentSubject.StudentSubjectListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "student_subjects",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        StudentSubjectListener::class
    ]
)
@Where(clause = "deleted = false")
data class StudentSubject(
    var uuidClassroomStudent: UUID? = null,
    var uuidSubject: UUID? = null,
    var uuidStudent: UUID? = null,
    var period: Int? = null,
    var uuidSchool: UUID? = null,
    var def: Double? = null,
    var recovery: Double? = null
) : BaseModel()
