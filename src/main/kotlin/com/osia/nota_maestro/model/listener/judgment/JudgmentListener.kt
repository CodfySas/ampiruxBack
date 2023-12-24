package com.osia.nota_maestro.model.listener.judgment

import com.osia.nota_maestro.model.Judgment
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.judgment.JudgmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class JudgmentListener : CodeSetter() {

    companion object {
        private lateinit var judgmentRepository: JudgmentRepository
    }

    @Autowired
    fun setProducer(_judgmentRepository: JudgmentRepository) {
        judgmentRepository = _judgmentRepository
    }

    @PrePersist
    fun prePersist(judgment: Judgment) {
        this.setCode(judgmentRepository, judgment)
    }
}
