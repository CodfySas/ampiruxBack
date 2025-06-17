package com.osia.ampirux.model.listener.saleserviceproduct

import com.osia.ampirux.model.SaleServiceProduct
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.saleserviceproduct.SaleServiceProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SaleServiceProductListener : CodeSetter() {

    companion object {
        private lateinit var saleserviceproductRepository: SaleServiceProductRepository
    }

    @Autowired
    fun setProducer(_saleserviceproductRepository: SaleServiceProductRepository) {
        saleserviceproductRepository = _saleserviceproductRepository
    }

    @PrePersist
    fun prePersist(saleserviceproduct: SaleServiceProduct) {
        this.setCode(saleserviceproductRepository, saleserviceproduct)
    }
}
