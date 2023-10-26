package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.worker.WorkerListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(
    name = "workers",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        WorkerListener::class
    ]
)
@Where(clause = "deleted = false")
data class Worker(
    var name: String? = null,
    var id: String? = null,
    var phone: String? = null,
    @NotNull
    var uuidCompany: UUID? = null
) : BaseModel()
