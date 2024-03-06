package com.osia.nota_maestro.model.listener.accompanimentStudent

import com.osia.nota_maestro.model.AccompanimentStudent
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.accompanimentStudent.AccompanimentStudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class AccompanimentStudentListener : CodeSetter() {

    companion object {
        private lateinit var accompanimentStudentRepository: AccompanimentStudentRepository
    }

    @Autowired
    fun setProducer(_accompanimentStudentRepository: AccompanimentStudentRepository) {
        accompanimentStudentRepository = _accompanimentStudentRepository
    }

    @PrePersist
    fun prePersist(accompanimentStudent: AccompanimentStudent) {
        this.setCode(accompanimentStudentRepository, accompanimentStudent)
    }
}
