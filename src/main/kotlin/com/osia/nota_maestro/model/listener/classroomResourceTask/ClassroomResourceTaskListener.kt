package com.osia.nota_maestro.model.listener.classroomResourceTask

import com.osia.nota_maestro.model.ClassroomResourceTask
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.classroomResourceTask.ClassroomResourceTaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClassroomResourceTaskListener : CodeSetter() {

    companion object {
        private lateinit var classroomResourceTaskRepository: ClassroomResourceTaskRepository
    }

    @Autowired
    fun setProducer(_classroomResourceTaskRepository: ClassroomResourceTaskRepository) {
        classroomResourceTaskRepository = _classroomResourceTaskRepository
    }

    @PrePersist
    fun prePersist(classroomResourceTask: ClassroomResourceTask) {
        this.setCode(classroomResourceTaskRepository, classroomResourceTask)
    }
}
