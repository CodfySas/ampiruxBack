package com.osia.logistic.need.service.client

import com.osia.logistic.need.dto.client.v1.ClientDto
import com.osia.logistic.need.dto.client.v1.ClientRequest
import com.osia.logistic.need.model.Client
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

interface ClientService {

    fun findByUuid(uuid: UUID): ClientDto

    fun findAll(pageable: Pageable, specs: Specification<Client>?): Page<ClientDto>

    fun save(clientRequest: ClientRequest): ClientDto

    fun update(uuid: UUID, clientRequest: ClientRequest): ClientDto

    fun delete(uuid: UUID): ClientDto

    fun getOne(uuid: UUID): Client

    fun findByUuidIn(clientList: List<UUID>): List<ClientDto>
}
