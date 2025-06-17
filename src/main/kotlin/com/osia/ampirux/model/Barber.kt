package com.osia.ampirux.model

import com.osia.ampirux.dto.barberschedule.v1.BarberScheduleDto
import com.osia.ampirux.model.abstracts.BaseModel
import com.osia.ampirux.model.listener.barber.BarberListener
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Table(
    name = "barbers",
)
@Entity
@DynamicUpdate
@EntityListeners(
    value = [
        BarberListener::class
    ]
)
@Where(clause = "deleted = false")
data class Barber(
    var name: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var barbershopUuid: UUID? = null,
    var dni: String? = null,
    var position: String? = null,
    var hireDate: LocalDate? = null,
    var baseSalary: BigDecimal? = null,
    var commissionRate: Double? = null,
    var status: String? = null,
) : BaseModel()
