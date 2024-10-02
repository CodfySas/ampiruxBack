package com.osia.nota_maestro.model.listener.preliminary

import com.osia.nota_maestro.model.Preliminary
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.preliminary.PreliminaryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class PreliminaryListener : CodeSetter() {

    companion object {
        private lateinit var preliminaryRepository: PreliminaryRepository
    }

    @Autowired
    fun setProducer(_preliminaryRepository: PreliminaryRepository) {
        preliminaryRepository = _preliminaryRepository
    }

    @PrePersist
    fun prePersist(preliminary: Preliminary) {
        this.setCode(preliminaryRepository, preliminary)
    }
}
