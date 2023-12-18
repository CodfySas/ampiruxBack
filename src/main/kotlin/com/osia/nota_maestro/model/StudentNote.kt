package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.studentNote.StudentNoteListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "student_notes",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        StudentNoteListener::class
    ]
)
@Where(clause = "deleted = false")
data class StudentNote(
    var uuidStudent: UUID? = null,
    var number: Int? = null,
    var noteName: String? = null,
    var note: Double? = null,
    var uuidClassroomStudent: UUID? = null,
    var uuidSubject: UUID? = null
) : BaseModel()
