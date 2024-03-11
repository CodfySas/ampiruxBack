package com.osia.nota_maestro.model.listener.classroomResource

import com.osia.nota_maestro.model.ClassroomResource
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.classroomResource.ClassroomResourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClassroomResourceListener : CodeSetter() {

    companion object {
        private lateinit var classroomResourceRepository: ClassroomResourceRepository
    }

    @Autowired
    fun setProducer(_classroomResourceRepository: ClassroomResourceRepository) {
        classroomResourceRepository = _classroomResourceRepository
    }

    @PrePersist
    fun prePersist(classroomResource: ClassroomResource) {
        this.setCode(classroomResourceRepository, classroomResource)
    }
}
