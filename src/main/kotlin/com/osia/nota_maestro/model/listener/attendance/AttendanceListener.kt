package com.osia.nota_maestro.model.listener.attendance

import com.osia.nota_maestro.model.Attendance
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.attendance.AttendanceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class AttendanceListener : CodeSetter() {

    companion object {
        private lateinit var attendanceRepository: AttendanceRepository
    }

    @Autowired
    fun setProducer(_attendanceRepository: AttendanceRepository) {
        attendanceRepository = _attendanceRepository
    }

    @PrePersist
    fun prePersist(attendance: Attendance) {
        this.setCode(attendanceRepository, attendance)
    }
}
