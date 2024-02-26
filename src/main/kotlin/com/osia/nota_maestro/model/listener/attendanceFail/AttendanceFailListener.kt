package com.osia.nota_maestro.model.listener.attendanceFail

import com.osia.nota_maestro.model.AttendanceFail
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.attendanceFail.AttendanceFailRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class AttendanceFailListener : CodeSetter() {

    companion object {
        private lateinit var attendanceFailRepository: AttendanceFailRepository
    }

    @Autowired
    fun setProducer(_attendanceFailRepository: AttendanceFailRepository) {
        attendanceFailRepository = _attendanceFailRepository
    }

    @PrePersist
    fun prePersist(attendanceFail: AttendanceFail) {
        this.setCode(attendanceFailRepository, attendanceFail)
    }
}
