package com.osia.ampirux.model.listener.saleproduct

import com.osia.ampirux.model.SaleProduct
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.saleproduct.SaleProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SaleProductListener : CodeSetter() {

    companion object {
        private lateinit var saleproductRepository: SaleProductRepository
    }

    @Autowired
    fun setProducer(_saleproductRepository: SaleProductRepository) {
        saleproductRepository = _saleproductRepository
    }

    @PrePersist
    fun prePersist(saleproduct: SaleProduct) {
        this.setCode(saleproductRepository, saleproduct)
    }
}
