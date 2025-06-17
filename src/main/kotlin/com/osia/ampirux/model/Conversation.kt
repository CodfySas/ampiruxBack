package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Table

@Table(
    name = "conversations",
)
@Entity
@DynamicUpdate
@Where(clause = "deleted = false")
data class Conversation(
    var userOneUuid: UUID? = null,
    var userTwoUuid: UUID? = null,
) : BaseModel()
