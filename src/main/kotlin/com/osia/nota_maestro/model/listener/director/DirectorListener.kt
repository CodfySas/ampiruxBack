package com.osia.nota_maestro.model.listener.director

import com.osia.nota_maestro.model.Director
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.director.DirectorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DirectorListener : CodeSetter() {

    companion object {
        private lateinit var directorRepository: DirectorRepository
    }

    @Autowired
    fun setProducer(_directorRepository: DirectorRepository) {
        directorRepository = _directorRepository
    }

    @PrePersist
    fun prePersist(director: Director) {
        this.setCode(directorRepository, director)
    }
}
