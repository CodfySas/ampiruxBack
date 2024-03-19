package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.examResponse.ExamResponseListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "exam_responses",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ExamResponseListener::class
    ]
)
@Where(clause = "deleted = false")
data class ExamResponse(
    var description: String? = null,
    var correct: Boolean? = null,
    var uuidExamQuestion: UUID? = null
) : BaseModel()
