package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.examUserResponse.ExamUserResponseListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "exam_user_responses",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ExamUserResponseListener::class
    ]
)
@Where(clause = "deleted = false")
data class ExamUserResponse(
    var uuidExamQuestion: UUID? = null,
    var uuidExamResponse: UUID? = null,
    var uuidAttempt: UUID? = null,
    var correct: Boolean? = null,
    var response: String? = null
) : BaseModel()
