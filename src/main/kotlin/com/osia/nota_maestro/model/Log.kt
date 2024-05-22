package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.log.LogListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "logs",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        LogListener::class
    ]
)
@Where(clause = "deleted = false")
data class Log(
    var uuidUser: UUID? = null,
    var movement: String? = null,
    var day: LocalDate? = null,
    var hour: String? = null,
    var status: String? = null,
    var detail: String? = null
) : BaseModel()
