package com.osia.nota_maestro.model.listener.directorStudent

import com.osia.nota_maestro.model.DirectorStudent
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.directorStudent.DirectorStudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DirectorStudentListener : CodeSetter() {

    companion object {
        private lateinit var directorStudentRepository: DirectorStudentRepository
    }

    @Autowired
    fun setProducer(_directorStudentRepository: DirectorStudentRepository) {
        directorStudentRepository = _directorStudentRepository
    }

    @PrePersist
    fun prePersist(directorStudent: DirectorStudent) {
        this.setCode(directorStudentRepository, directorStudent)
    }
}
