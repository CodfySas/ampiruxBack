package com.osia.ampirux.model.listener.discount

import com.osia.ampirux.model.Discount
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.discount.DiscountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DiscountListener : CodeSetter() {

    companion object {
        private lateinit var discountsRepository: DiscountRepository
    }

    @Autowired
    fun setProducer(_discountsRepository: DiscountRepository) {
        discountsRepository = _discountsRepository
    }

    @PrePersist
    fun prePersist(discounts: Discount) {
        this.setCode(discountsRepository, discounts)
    }
}
