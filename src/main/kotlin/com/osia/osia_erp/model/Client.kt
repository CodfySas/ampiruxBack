package com.osia.osia_erp.model

import com.osia.osia_erp.model.abstracts.BaseModel
import com.osia.osia_erp.model.listener.client.ClientListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "clients",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        ClientListener::class
    ]
)
@Where(clause = "deleted = false")
data class Client(
    var name: String? = null,
    var id: String? = null,
    var phone: String? = null,
    @NotNull
    var uuidCompany: UUID? = null
) : BaseModel()
