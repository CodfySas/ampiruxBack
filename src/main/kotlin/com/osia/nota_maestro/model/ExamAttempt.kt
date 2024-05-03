package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.examAttempt.ExamAttemptListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "exam_attempts",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ExamAttemptListener::class
    ]
)
@Where(clause = "deleted = false")
data class ExamAttempt(
    var uuidStudent: UUID? = null,
    var uuidExam: UUID? = null,
    var note: Double? = null,
    var observation: String? = null
) : BaseModel()
