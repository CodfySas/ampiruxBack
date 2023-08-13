package com.osia.osia_erp.model

import com.osia.osia_erp.model.abstracts.BaseModel
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "modules",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        AuditingEntityListener::class
    ]
)
@Where(clause = "deleted = false")
data class Module(
    var name: String? = null,
    var order: Int? = null
) : BaseModel()
