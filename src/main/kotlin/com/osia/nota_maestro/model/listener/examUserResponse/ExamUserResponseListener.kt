package com.osia.nota_maestro.model.listener.examUserResponse

import com.osia.nota_maestro.model.ExamUserResponse
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.examUserResponse.ExamUserResponseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ExamUserResponseListener : CodeSetter() {

    companion object {
        private lateinit var examUserResponseRepository: ExamUserResponseRepository
    }

    @Autowired
    fun setProducer(_examUserResponseRepository: ExamUserResponseRepository) {
        examUserResponseRepository = _examUserResponseRepository
    }

    @PrePersist
    fun prePersist(examUserResponse: ExamUserResponse) {
        this.setCode(examUserResponseRepository, examUserResponse)
    }
}
