package com.osia.nota_maestro.model.listener.examResponse

import com.osia.nota_maestro.model.ExamResponse
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.examResponse.ExamResponseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ExamResponseListener : CodeSetter() {

    companion object {
        private lateinit var examResponseRepository: ExamResponseRepository
    }

    @Autowired
    fun setProducer(_examResponseRepository: ExamResponseRepository) {
        examResponseRepository = _examResponseRepository
    }

    @PrePersist
    fun prePersist(examResponse: ExamResponse) {
        this.setCode(examResponseRepository, examResponse)
    }
}
