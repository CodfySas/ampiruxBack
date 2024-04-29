package com.osia.nota_maestro.model.listener.diagnosis

import com.osia.nota_maestro.model.Diagnosis
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.diagnosis.DiagnosisRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DiagnosisListener : CodeSetter() {

    companion object {
        private lateinit var diagnosisRepository: DiagnosisRepository
    }

    @Autowired
    fun setProducer(_diagnosisRepository: DiagnosisRepository) {
        diagnosisRepository = _diagnosisRepository
    }

    @PrePersist
    fun prePersist(diagnosis: Diagnosis) {
        this.setCode(diagnosisRepository, diagnosis)
    }
}
