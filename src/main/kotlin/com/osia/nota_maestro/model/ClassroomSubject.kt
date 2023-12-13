package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.classroomSubject.ClassroomSubjectListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "classroom_subjects",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClassroomSubjectListener::class
    ]
)
@Where(clause = "deleted = false")
data class ClassroomSubject(
    var uuidTeacher: UUID? = null,
    var uuidSubject: UUID? = null,
    var uuidClassroom: UUID? = null
) : BaseModel()
