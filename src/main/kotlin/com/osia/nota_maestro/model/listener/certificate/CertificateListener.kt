package com.osia.nota_maestro.model.listener.certificate

import com.osia.nota_maestro.model.Certificate
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.certificate.CertificateRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class CertificateListener : CodeSetter() {

    companion object {
        private lateinit var certificateRepository: CertificateRepository
    }

    @Autowired
    fun setProducer(_certificateRepository: CertificateRepository) {
        certificateRepository = _certificateRepository
    }

    @PrePersist
    fun prePersist(certificate: Certificate) {
        this.setCode(certificateRepository, certificate)
    }
}
