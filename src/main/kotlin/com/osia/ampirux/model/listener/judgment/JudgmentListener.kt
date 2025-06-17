package com.osia.ampirux.model.listener.judgment

import com.osia.ampirux.model.Judgment
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.judgment.JudgmentRepository
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
