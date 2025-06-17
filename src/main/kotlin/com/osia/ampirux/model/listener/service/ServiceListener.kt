package com.osia.ampirux.model.listener.service

import com.osia.ampirux.model.Service
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.service.ServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ServiceListener : CodeSetter() {

    companion object {
        private lateinit var servicesRepository: ServiceRepository
    }

    @Autowired
    fun setProducer(_servicesRepository: ServiceRepository) {
        servicesRepository = _servicesRepository
    }

    @PrePersist
    fun prePersist(services: Service) {
        this.setCode(servicesRepository, services)
    }
}
