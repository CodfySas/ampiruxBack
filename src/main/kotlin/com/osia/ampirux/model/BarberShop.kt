package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.barbershop.BarberShopListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "barber_shops",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        BarberShopListener::class
    ]
)
@Where(clause = "deleted = false")
data class BarberShop(
    var name: String? = null,
    var nit: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var logoUrl: String? = null,
    var defaultCashBase: BigDecimal? = null,
    var ivaPercent: Double? = null,
    var applyIva: Boolean? = null,
    var retentionPercent: Double? = null
) : BaseModel()

