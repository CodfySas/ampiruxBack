package com.osia.ampirux.model.listener.sale

import com.osia.ampirux.model.Sale
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.sale.SaleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SaleListener : CodeSetter() {

    companion object {
        private lateinit var saleRepository: SaleRepository
    }

    @Autowired
    fun setProducer(_saleRepository: SaleRepository) {
        saleRepository = _saleRepository
    }

    @PrePersist
    fun prePersist(sale: Sale) {
        this.setCode(saleRepository, sale)
    }
}
