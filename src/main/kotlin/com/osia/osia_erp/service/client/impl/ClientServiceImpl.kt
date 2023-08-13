package com.osia.osia_erp.service.client.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.osia_erp.dto.client.v1.ClientDto
import com.osia.osia_erp.dto.client.v1.ClientMapper
import com.osia.osia_erp.dto.client.v1.ClientRequest
import com.osia.osia_erp.model.Client
import com.osia.osia_erp.repository.client.ClientRepository
import com.osia.osia_erp.service.client.ClientService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("client.crud_service")
@Transactional
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
    private val objectMapper: ObjectMapper
) : ClientService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("client count -> increment: $increment")
        return clientRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Client {
        return clientRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Client $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClientDto> {
        log.trace("client findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return clientRepository.findAllById(uuidList).map(clientMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<ClientDto> {
        log.trace("client findAll -> pageable: $pageable")
        return clientRepository.findAll(pageable).map(clientMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<ClientDto> {
        log.trace("client findAllByFilter -> pageable: $pageable, where: $where")
        return clientRepository.findAll(Specification.where(createSpec(where)), pageable).map(clientMapper::toDto)
    }

    @Transactional
    override fun save(clientRequest: ClientRequest): ClientDto {
        log.trace("client save -> request: $clientRequest")
        val client = clientMapper.toModel(clientRequest)
        return clientMapper.toDto(clientRepository.save(client))
    }

    @Transactional
    override fun saveMultiple(clientRequestList: List<ClientRequest>): List<ClientDto> {
        log.trace("client saveMultiple -> requestList: ${objectMapper.writeValueAsString(clientRequestList)}")
        val clients = clientRequestList.map(clientMapper::toModel)
        return clientRepository.saveAll(clients).map(clientMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, clientRequest: ClientRequest): ClientDto {
        log.trace("client update -> uuid: $uuid, request: $clientRequest")
        val client = getById(uuid)
        clientMapper.update(clientRequest, client)
        return clientMapper.toDto(clientRepository.save(client))
    }

    @Transactional
    override fun updateMultiple(clientDtoList: List<ClientDto>): List<ClientDto> {
        log.trace("client updateMultiple -> clientDtoList: ${objectMapper.writeValueAsString(clientDtoList)}")
        val clients = clientRepository.findAllById(clientDtoList.mapNotNull { it.uuid })
        clients.forEach { client ->
            clientMapper.update(clientMapper.toRequest(clientDtoList.first { it.uuid == client.uuid }), client)
        }
        return clientRepository.saveAll(clients).map(clientMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("client delete -> uuid: $uuid")
        val client = getById(uuid)
        client.deleted = true
        client.deletedAt = LocalDateTime.now()
        clientRepository.save(client)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("client deleteMultiple -> uuid: $uuidList")
        val clients = clientRepository.findAllById(uuidList)
        clients.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        clientRepository.saveAll(clients)
    }

    fun createSpec(where: String): Specification<Client> {
        var finalSpec = Specification { root: Root<Client>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<Client>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
