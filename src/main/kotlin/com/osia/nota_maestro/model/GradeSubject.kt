package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.gradeSubject.GradeSubjectListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "grade_subjects",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        GradeSubjectListener::class
    ]
)
@Where(clause = "deleted = false")
data class GradeSubject(
        var uuidGrade: UUID? = null,
        var uuidSubject: UUID? = null,
        var uuidTeacher: UUID? = null
) : BaseModel()
