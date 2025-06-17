package com.osia.ampirux.model.listener.barberSchedule

import com.osia.ampirux.model.BarberSchedule
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.barberSchedule.BarberScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class BarberScheduleListener : CodeSetter() {

    companion object {
        private lateinit var barberScheduleRepository: BarberScheduleRepository
    }

    @Autowired
    fun setProducer(_barberScheduleRepository: BarberScheduleRepository) {
        barberScheduleRepository = _barberScheduleRepository
    }

    @PrePersist
    fun prePersist(barberSchedule: BarberSchedule) {
        this.setCode(barberScheduleRepository, barberSchedule)
    }
}
