package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.service.ServiceListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "services",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ServiceListener::class
    ]
)
@Where(clause = "deleted = false")
data class Service(
    var name: String? = null,
    var description: String? = null,
    var price: BigDecimal? = null,
    var durationMinutes: Int? = null,
    var popularity: Int? = null,
    var barbershopUuid: UUID? = null
) : BaseModel()
