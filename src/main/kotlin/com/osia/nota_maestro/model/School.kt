package com.osia.nota_maestro.model

import com.osia.nota_maestro.model.abstracts.BaseModel
import com.osia.nota_maestro.model.listener.school.SchoolListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "schools",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        SchoolListener::class
    ]
)
@Where(clause = "deleted = false")
data class School(
    var name: String? = null,
    var shortName: String? = null,
    var color1: String? = "E78617",
    var color2: String? = "e35205",
    var active: Boolean? = true,
    var expireDate: LocalDateTime? = null,
    var periods: Int? = null,
    var actualYear: Int? = null,
    var enabledTeacher: Boolean? = null,
    var enabledStudent: Boolean? = null,
    var maxNote: Double? = null,
    var minNote: Double? = null,
    var toLose: Int? = null,
    var recoveryType: String? = null,
    var enabledFinalRecovery: Boolean? = null,
    var directorName: String? = null,
    var directorDni: String? = null,
    var directorRole: String? = null,
    var signReportType: String? = null,
    var certificates: String? = null,
    var periodAvailable: String? = null,
    var reportType: String? = null,
    var planningType: String? = null
) : BaseModel()
