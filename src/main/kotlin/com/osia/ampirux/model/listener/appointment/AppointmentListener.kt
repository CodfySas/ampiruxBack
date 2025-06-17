package com.osia.ampirux.model.listener.appointment

import com.osia.ampirux.model.Appointment
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.appointment.AppointmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class AppointmentListener : CodeSetter() {

    companion object {
        private lateinit var appointmentRepository: AppointmentRepository
    }

    @Autowired
    fun setProducer(_appointmentRepository: AppointmentRepository) {
        appointmentRepository = _appointmentRepository
    }

    @PrePersist
    fun prePersist(appointment: Appointment) {
        this.setCode(appointmentRepository, appointment)
    }
}
