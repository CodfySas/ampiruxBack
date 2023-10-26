package com.osia.nota_maestro.model.listener.client

import com.osia.nota_maestro.model.Client
import com.osia.nota_maestro.model.abstracts.CodeSetter
import com.osia.nota_maestro.repository.client.ClientRepository
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
