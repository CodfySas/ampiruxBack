package com.osia.osia_erp.model

import com.osia.osia_erp.model.abstracts.BaseModel
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "sub_module_users",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AuditingEntityListener::class,
    ]
)
@Where(clause = "deleted = false")
data class SubModuleUser(
    var uuidUser: UUID? = null,
    var uuidSubModule: UUID? = null
) : BaseModel()
