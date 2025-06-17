package com.osia.ampirux.model.listener.commission

import com.osia.ampirux.model.Commission
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.commission.CommissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class CommissionListener : CodeSetter() {

    companion object {
        private lateinit var commissionRepository: CommissionRepository
    }

    @Autowired
    fun setProducer(_commissionRepository: CommissionRepository) {
        commissionRepository = _commissionRepository
    }

    @PrePersist
    fun prePersist(commission: Commission) {
        this.setCode(commissionRepository, commission)
    }
}
