package com.osia.nota_maestro.model.listener.studentPosition

import com.osia.nota_maestro.model.StudentPosition
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.studentPosition.StudentPositionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class StudentPositionListener : CodeSetter() {

    companion object {
        private lateinit var studentPositionRepository: StudentPositionRepository
    }

    @Autowired
    fun setProducer(_studentPositionRepository: StudentPositionRepository) {
        studentPositionRepository = _studentPositionRepository
    }

    @PrePersist
    fun prePersist(studentPosition: StudentPosition) {
        this.setCode(studentPositionRepository, studentPosition)
    }
}
