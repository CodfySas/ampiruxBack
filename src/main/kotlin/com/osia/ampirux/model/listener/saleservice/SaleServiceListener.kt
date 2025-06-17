package com.osia.ampirux.model.listener.saleservice

import com.osia.ampirux.model.SaleService
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.saleservice.SaleServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class SaleServiceListener : CodeSetter() {

    companion object {
        private lateinit var saleserviceRepository: SaleServiceRepository
    }

    @Autowired
    fun setProducer(_saleserviceRepository: SaleServiceRepository) {
        saleserviceRepository = _saleserviceRepository
    }

    @PrePersist
    fun prePersist(saleservice: SaleService) {
        this.setCode(saleserviceRepository, saleservice)
    }
}
