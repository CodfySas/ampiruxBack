package com.osia.osia_erp.model

import com.osia.osia_erp.model.abstracts.BaseModel
import com.osia.osia_erp.model.listener.company.CompanyListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "companies",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        CompanyListener::class
    ]
)
@Where(clause = "deleted = false")
data class Company(
    var name: String? = null,
    var active: Boolean? = true,
    var expireDate: LocalDateTime? = null
) : BaseModel()
