package com.osia.osia_erp.model.listener.company

import com.osia.osia_erp.model.Company
import com.osia.osia_erp.model.abstracts.CodeSetter
import com.osia.osia_erp.repository.company.CompanyRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import javax.persistence.PostPersist
import javax.persistence.PostUpdate
import javax.persistence.PrePersist

@Component
class CompanyListener : CodeSetter() {
    companion object {
        lateinit var companyRepository: CompanyRepository
    }

    @Autowired
    fun setCompanion(

        companyRepository1: CompanyRepository
    ) {
        companyRepository = companyRepository1
    }

    private val log = LoggerFactory.getLogger(javaClass)

    @PrePersist
    fun prePersist(company: Company) {
        this.setCode(companyRepository, company)
        company.expireDate = LocalDateTime.now().plusMonths(1)
        log.trace("prePersist -> company: $company")
    }

    @PostPersist
    fun postPersist(company: Company) {
        log.trace("postPersist -> company: $company")
    }

    @PostUpdate
    fun postUpdate(company: Company) {
        log.trace("postUpdate -> company: $company")
    }
}
