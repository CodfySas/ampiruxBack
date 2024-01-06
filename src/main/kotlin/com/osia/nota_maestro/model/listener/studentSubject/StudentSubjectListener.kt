package com.osia.nota_maestro.model.listener.studentSubject

import com.osia.nota_maestro.model.StudentSubject
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class StudentSubjectListener : CodeSetter() {

    companion object {
        private lateinit var studentSubjectRepository: StudentSubjectRepository
    }

    @Autowired
    fun setProducer(_studentSubjectRepository: StudentSubjectRepository) {
        studentSubjectRepository = _studentSubjectRepository
    }

    @PrePersist
    fun prePersist(studentSubject: StudentSubject) {
        this.setCode(studentSubjectRepository, studentSubject)
    }
}
