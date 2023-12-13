package com.osia.nota_maestro.model.listener.classroomSubject

import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClassroomSubjectListener : CodeSetter() {

    companion object {
        private lateinit var classroomSubjectRepository: ClassroomSubjectRepository
    }

    @Autowired
    fun setProducer(_classroomSubjectRepository: ClassroomSubjectRepository) {
        classroomSubjectRepository = _classroomSubjectRepository
    }

    @PrePersist
    fun prePersist(classroomSubject: ClassroomSubject) {
        this.setCode(classroomSubjectRepository, classroomSubject)
    }
}
