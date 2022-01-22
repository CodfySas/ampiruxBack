package com.osia.logistic.need.model.listener.client

import com.osia.logistic.need.model.Client
import com.osia.logistic.need.model.abstracts.CodeSetter
import com.osia.logistic.need.repository.client.ClientRepository
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
