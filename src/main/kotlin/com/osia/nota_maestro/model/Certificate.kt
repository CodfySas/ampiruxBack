package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.certificate.CertificateListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "certificates",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        CertificateListener::class
    ]
)
@Where(clause = "deleted = false")
data class Certificate(
    var type: String? = null,
    var approved: Boolean? = null,
    var uuidUser: UUID? = null,
    var uuidSchool: UUID? = null,
    var approvedAt: LocalDateTime? = null,
    var actualGrade: String? = null,
    var initTime: String? = null,
    var position: String? = null,
    var status: String? = null,
    var term: String? = null,
    var salary: String? = null
) : BaseModel()
