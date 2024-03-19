package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.examQuestion.ExamQuestionListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "exam_questions",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ExamQuestionListener::class
    ]
)
@Where(clause = "deleted = false")
data class ExamQuestion(
    var description: String? = null,
    var type: String? = null,
    var uuidExam: UUID? = null,
    var ordered: Int? = null
) : BaseModel()
