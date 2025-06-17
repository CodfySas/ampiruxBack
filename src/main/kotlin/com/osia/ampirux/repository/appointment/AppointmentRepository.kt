package com.osia.ampirux.repository.appointment
import com.osia.ampirux.model.Appointment
import com.osia.ampirux.repository.CommonRepository
import org.springframework.stereotype.Repository

@Repository("appointment.crud_repository")
interface AppointmentRepository : CommonRepository<Appointment>
