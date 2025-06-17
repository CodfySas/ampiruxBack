package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Table

@Table(
    name = "messages",
)
@Entity
@DynamicUpdate
@Where(clause = "deleted = false")
data class Message(
    var message: String? = null,
    var senderUuid: UUID? = null,
    var receiverUuid: UUID? = null,
    var conversationUuid: UUID? = null,
    var read: Boolean? = false
) : BaseModel()
