package com.osia.nota_maestro.model.listener.mesh

import com.osia.nota_maestro.model.Mesh
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.mesh.MeshRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class MeshListener : CodeSetter() {

    companion object {
        private lateinit var meshRepository: MeshRepository
    }

    @Autowired
    fun setProducer(_meshRepository: MeshRepository) {
        meshRepository = _meshRepository
    }

    @PrePersist
    fun prePersist(mesh: Mesh) {
        this.setCode(meshRepository, mesh)
    }
}
