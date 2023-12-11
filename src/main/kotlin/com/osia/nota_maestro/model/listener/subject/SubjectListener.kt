package com.osia.nota_maestro.model.listener.subject

import com.osia.nota_maestro.model.Subject
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.subject.SubjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SubjectListener : CodeSetter() {

    companion object {
        private lateinit var subjectRepository: SubjectRepository
    }

    @Autowired
    fun setProducer(_subjectRepository: SubjectRepository) {
        subjectRepository = _subjectRepository
    }

    @PrePersist
    fun prePersist(subject: Subject) {
        this.setCode(subjectRepository, subject)
    }
}
