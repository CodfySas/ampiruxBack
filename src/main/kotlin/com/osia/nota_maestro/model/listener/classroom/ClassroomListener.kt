package com.osia.nota_maestro.model.listener.classroom

import com.osia.nota_maestro.model.Classroom
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClassroomListener : CodeSetter() {

    companion object {
        private lateinit var classroomRepository: ClassroomRepository
    }

    @Autowired
    fun setProducer(_classroomRepository: ClassroomRepository) {
        classroomRepository = _classroomRepository
    }

    @PrePersist
    fun prePersist(classroom: Classroom) {
        this.setCode(classroomRepository, classroom)
    }
}
