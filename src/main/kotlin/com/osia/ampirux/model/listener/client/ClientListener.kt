package com.osia.ampirux.model.listener.client

import com.osia.ampirux.model.Client
import com.osia.ampirux.model.abstracts.CodeSetter
import com.osia.ampirux.repository.client.ClientRepository
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
