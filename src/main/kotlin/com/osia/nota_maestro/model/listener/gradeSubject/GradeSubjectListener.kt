package com.osia.nota_maestro.model.listener.gradeSubject

import com.osia.nota_maestro.model.GradeSubject
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class GradeSubjectListener : CodeSetter() {

    companion object {
        private lateinit var gradeSubjectRepository: GradeSubjectRepository
    }

    @Autowired
    fun setProducer(_gradeSubjectRepository: GradeSubjectRepository) {
        gradeSubjectRepository = _gradeSubjectRepository
    }

    @PrePersist
    fun prePersist(gradeSubject: GradeSubject) {
        this.setCode(gradeSubjectRepository, gradeSubject)
    }
}
