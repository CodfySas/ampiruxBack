package com.osia.ampirux.repository.appointment
import com.osia.ampirux.model.Appointment
import com.osia.ampirux.repository.CommonRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Repository("appointment.crud_repository")
interface AppointmentRepository : CommonRepository<Appointment> {

    @Query(value = "SELECT COUNT(*) FROM appointments", nativeQuery = true)
    override fun count(): Long

    fun findAllByScheduledAtBetweenAndBarbershopUuidOrderByScheduledAt(init: LocalDateTime, finish: LocalDateTime, barberShopUuid: UUID): List<Appointment>

}
