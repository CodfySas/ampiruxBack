package com.osia.ampirux.model.listener.discountservice

import com.osia.ampirux.model.DiscountService
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.discountservice.DiscountServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DiscountServiceListener : CodeSetter() {

    companion object {
        private lateinit var discountserviceRepository: DiscountServiceRepository
    }

    @Autowired
    fun setProducer(_discountserviceRepository: DiscountServiceRepository) {
        discountserviceRepository = _discountserviceRepository
    }

    @PrePersist
    fun prePersist(discountservice: DiscountService) {
        this.setCode(discountserviceRepository, discountservice)
    }
}
