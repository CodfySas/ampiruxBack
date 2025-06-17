package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.judgment.JudgmentListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "judgments",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        JudgmentListener::class
    ]
)
@Where(clause = "deleted = false")
data class Judgment(
    var name: String? = null,
    var entryOn: Int? = null,
    var uuidSubject: UUID? = null,
    var uuidGrade: UUID? = null,
    var period: Int? = null,
) : BaseModel()
