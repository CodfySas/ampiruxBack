package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.servicedefaultproduct.ServiceDefaultProductListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "service_default_products",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ServiceDefaultProductListener::class
    ]
)
@Where(clause = "deleted = false")
data class ServiceDefaultProduct(
    var serviceUuid: UUID? = null,
    var productUuid: UUID? = null,
    var quantity: Double? = null,
    var unit: String? = null, // "ml", "g", "units", etc.
    var costType: String? = null, // "client", "courtesy", "barber"
) : BaseModel()
