package com.osia.ampirux.model.listener.cash

import com.osia.ampirux.model.Cash
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.cash.CashRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class CashListener : CodeSetter() {

    companion object {
        private lateinit var cashRepository: CashRepository
    }

    @Autowired
    fun setProducer(_cashRepository: CashRepository) {
        cashRepository = _cashRepository
    }

    @PrePersist
    fun prePersist(cash: Cash) {
        this.setCode(cashRepository, cash)
    }
}
