package com.osia.nota_maestro.model.listener.grade

import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.grade.GradeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class GradeListener : CodeSetter() {

    companion object {
        private lateinit var gradeRepository: GradeRepository
    }

    @Autowired
    fun setProducer(_gradeRepository: GradeRepository) {
        gradeRepository = _gradeRepository
    }

    @PrePersist
    fun prePersist(grade: Grade) {
        this.setCode(gradeRepository, grade)
    }
}
