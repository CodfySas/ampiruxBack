package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.enums.NotificationEnum
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(
    name = "notifications",
)
@Entity
@DynamicUpdate
@Where(clause = "deleted = false")
data class Notification(
    var userUuid: UUID? = null,
    var senderUuid: UUID? = null,
    @Enumerated(EnumType.STRING)
    var type: NotificationEnum? = null,
    var message: String = "",
    var read: Boolean = false,
    var link: String = "",
    var postUuid: UUID? = null
) : BaseModel()
