package com.osia.ampirux.model

import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.client.ClientListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

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
    var phone: String? = null,
    var email: String? = null,
    var preferredBarberUuid: UUID? = null,
    var dni: String? = null,
    var notes: String? = null,
    var barbershopUuid: UUID? = null,
) : BaseModel()
