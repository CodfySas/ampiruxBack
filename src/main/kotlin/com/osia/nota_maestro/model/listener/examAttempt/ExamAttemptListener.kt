package com.osia.nota_maestro.model.listener.examAttempt

import com.osia.nota_maestro.model.ExamAttempt
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.examAttempt.ExamAttemptRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ExamAttemptListener : CodeSetter() {

    companion object {
        private lateinit var examAttemptRepository: ExamAttemptRepository
    }

    @Autowired
    fun setProducer(_examAttemptRepository: ExamAttemptRepository) {
        examAttemptRepository = _examAttemptRepository
    }

    @PrePersist
    fun prePersist(examAttempt: ExamAttempt) {
        this.setCode(examAttemptRepository, examAttempt)
    }
}
