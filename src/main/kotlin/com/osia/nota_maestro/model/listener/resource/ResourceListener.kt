package com.osia.nota_maestro.model.listener.resource

import com.osia.nota_maestro.model.Resource
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.resource.ResourceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ResourceListener : CodeSetter() {

    companion object {
        private lateinit var resourceRepository: ResourceRepository
    }

    @Autowired
    fun setProducer(_resourceRepository: ResourceRepository) {
        resourceRepository = _resourceRepository
    }

    @PrePersist
    fun prePersist(resource: Resource) {
        this.setCode(resourceRepository, resource)
    }
}
