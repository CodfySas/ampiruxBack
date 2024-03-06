package com.osia.nota_maestro.model.listener.accompaniment

import com.osia.nota_maestro.model.Accompaniment
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.accompaniment.AccompanimentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class AccompanimentListener : CodeSetter() {

    companion object {
        private lateinit var accompanimentRepository: AccompanimentRepository
    }

    @Autowired
    fun setProducer(_accompanimentRepository: AccompanimentRepository) {
        accompanimentRepository = _accompanimentRepository
    }

    @PrePersist
    fun prePersist(accompaniment: Accompaniment) {
        this.setCode(accompanimentRepository, accompaniment)
    }
}
