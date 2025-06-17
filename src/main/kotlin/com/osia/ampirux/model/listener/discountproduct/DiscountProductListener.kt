package com.osia.ampirux.model.listener.discountproduct

import com.osia.ampirux.model.DiscountProduct
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.discountproduct.DiscountProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class DiscountProductListener : CodeSetter() {

    companion object {
        private lateinit var discountproductRepository: DiscountProductRepository
    }

    @Autowired
    fun setProducer(_discountproductRepository: DiscountProductRepository) {
        discountproductRepository = _discountproductRepository
    }

    @PrePersist
    fun prePersist(discountproduct: DiscountProduct) {
        this.setCode(discountproductRepository, discountproduct)
    }
}
