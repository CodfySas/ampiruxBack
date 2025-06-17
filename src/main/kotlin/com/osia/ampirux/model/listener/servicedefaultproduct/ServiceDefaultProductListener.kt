package com.osia.ampirux.model.listener.servicedefaultproduct

import com.osia.ampirux.model.ServiceDefaultProduct
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.servicedefaultproduct.ServiceDefaultProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ServiceDefaultProductListener : CodeSetter() {

    companion object {
        private lateinit var servicedefaultproductsRepository: ServiceDefaultProductRepository
    }

    @Autowired
    fun setProducer(_servicedefaultproductsRepository: ServiceDefaultProductRepository) {
        servicedefaultproductsRepository = _servicedefaultproductsRepository
    }

    @PrePersist
    fun prePersist(servicedefaultproducts: ServiceDefaultProduct) {
        this.setCode(servicedefaultproductsRepository, servicedefaultproducts)
    }
}
