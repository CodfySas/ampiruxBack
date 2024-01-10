package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.notification.NotificationListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "notifications",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        NotificationListener::class
    ]
)
@Where(clause = "deleted = false")
data class Notification(
    var uuidUser: UUID? = null,
    var description: String? = null,
    var urlLink: String? = null,
    var uuidSchool: String? = null,
    var viewed: Boolean? = false,
    var datetime: LocalDateTime? = null,
    var type: String? = null
) : BaseModel()
