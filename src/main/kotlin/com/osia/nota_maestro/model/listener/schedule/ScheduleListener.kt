package com.osia.nota_maestro.model.listener.schedule

import com.osia.nota_maestro.model.Schedule
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.schedule.ScheduleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ScheduleListener : CodeSetter() {

    companion object {
        private lateinit var scheduleRepository: ScheduleRepository
    }

    @Autowired
    fun setProducer(_scheduleRepository: ScheduleRepository) {
        scheduleRepository = _scheduleRepository
    }

    @PrePersist
    fun prePersist(schedule: Schedule) {
        this.setCode(scheduleRepository, schedule)
    }
}
