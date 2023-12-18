package com.osia.nota_maestro.model.listener.schoolPeriod

import com.osia.nota_maestro.model.SchoolPeriod
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SchoolPeriodListener : CodeSetter() {

    companion object {
        private lateinit var schoolPeriodRepository: SchoolPeriodRepository
    }

    @Autowired
    fun setProducer(_schoolPeriodRepository: SchoolPeriodRepository) {
        schoolPeriodRepository = _schoolPeriodRepository
    }

    @PrePersist
    fun prePersist(schoolPeriod: SchoolPeriod) {
        this.setCode(schoolPeriodRepository, schoolPeriod)
    }
}
