package com.osia.nota_maestro.service.client

import com.osia.nota_maestro.dto.client.v1.ClientDto
import com.osia.nota_maestro.dto.client.v1.ClientRequest
import com.osia.nota_maestro.model.Client
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ClientService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Client
    fun findByMultiple(uuidList: List<UUID>): List<ClientDto>
    fun findAll(pageable: Pageable): Page<ClientDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<ClientDto>
    // Create
    fun save(clientRequest: ClientRequest): ClientDto
    fun saveMultiple(clientRequestList: List<ClientRequest>): List<ClientDto>
    // Update
    fun update(uuid: UUID, clientRequest: ClientRequest): ClientDto
    fun updateMultiple(clientDtoList: List<ClientDto>): List<ClientDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
