package com.osia.osia_erp.model.listener.client

import com.osia.osia_erp.model.Client
import com.osia.osia_erp.model.abstracts.CodeSetter
import com.osia.osia_erp.repository.client.ClientRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.PrePersist

@Component
class ClientListener : CodeSetter() {

    companion object {
        private lateinit var clientRepository: ClientRepository
    }

    @Autowired
    fun setProducer(_clientRepository: ClientRepository) {
        clientRepository = _clientRepository
    }

    @PrePersist
    fun prePersist(client: Client) {
        this.setCode(clientRepository, client)
    }
}
