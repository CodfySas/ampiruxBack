package com.osia.nota_maestro.model.listener.classroomStudent

import com.osia.nota_maestro.model.ClassroomStudent
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClassroomStudentListener : CodeSetter() {

    companion object {
        private lateinit var classroomStudentRepository: ClassroomStudentRepository
    }

    @Autowired
    fun setProducer(_classroomStudentRepository: ClassroomStudentRepository) {
        classroomStudentRepository = _classroomStudentRepository
    }

    @PrePersist
    fun prePersist(classroomStudent: ClassroomStudent) {
        this.setCode(classroomStudentRepository, classroomStudent)
    }
}
