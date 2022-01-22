package com.osia.logistic.need.service.client.impl

import com.osia.logistic.need.dto.client.v1.ClientDto
import com.osia.logistic.need.dto.client.v1.ClientMapper
import com.osia.logistic.need.dto.client.v1.ClientRequest
import com.osia.logistic.need.model.Client
import com.osia.logistic.need.repository.client.ClientRepository
import com.osia.logistic.need.service.client.ClientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service("client.crud_service")
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
) : ClientService {

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, specs: Specification<Client>?): Page<ClientDto> {
        return clientRepository.findAll(Specification.where(specs), pageable).map(clientMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: UUID): ClientDto {
        return clientMapper.toDto(getOne(uuid))
    }

    @Transactional
    override fun save(clientRequest: ClientRequest): ClientDto {
        val client = clientMapper.toModel(clientRequest)
        return clientMapper.toDto(clientRepository.save(client))
    }

    @Transactional
    override fun update(uuid: UUID, clientRequest: ClientRequest): ClientDto {
        val client = getOne(uuid)
        clientMapper.updateModel(clientRequest, client)
        return clientMapper.toDto(clientRepository.save(client))
    }

    override fun delete(uuid: UUID): ClientDto {
        val client = getOne(uuid)
        val clientDto = clientMapper.toDto(client)
        clientRepository.delete(client)
        return clientDto
    }

    override fun findByUuidIn(clientList: List<UUID>): List<ClientDto> {
        return clientRepository.findByUuidIn(clientList).map(clientMapper::toDto)
    }

    override fun getOne(uuid: UUID): Client {
        return clientRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "client $uuid not found")
        }
    }
}
