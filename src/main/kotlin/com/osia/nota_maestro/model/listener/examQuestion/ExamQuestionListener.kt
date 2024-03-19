package com.osia.nota_maestro.model.listener.examQuestion

import com.osia.nota_maestro.model.ExamQuestion
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.examQuestion.ExamQuestionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ExamQuestionListener : CodeSetter() {

    companion object {
        private lateinit var examQuestionRepository: ExamQuestionRepository
    }

    @Autowired
    fun setProducer(_examQuestionRepository: ExamQuestionRepository) {
        examQuestionRepository = _examQuestionRepository
    }

    @PrePersist
    fun prePersist(examQuestion: ExamQuestion) {
        this.setCode(examQuestionRepository, examQuestion)
    }
}
