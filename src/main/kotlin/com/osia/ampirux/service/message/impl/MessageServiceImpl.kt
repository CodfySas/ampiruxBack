package com.osia.ampirux.service.message.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.controller.message.v1.MessageWSController
import com.osia.ampirux.dto.BaseMapper
import com.osia.ampirux.dto.message.v1.MessageDto
import com.osia.ampirux.dto.message.v1.MessageRequest
import com.osia.ampirux.dto.user.v1.UserMapper
import com.osia.ampirux.model.Message
import com.osia.ampirux.repository.message.MessageRepository
import com.osia.ampirux.service.message.MessageService
import com.osia.ampirux.service.user.UserService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@Transactional
@Service
class MessageServiceImpl(
    private val repository: MessageRepository,
    private val mapper: BaseMapper<MessageRequest, Message, MessageDto>,
    private val objectMapper: ObjectMapper,
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val messageWSController: MessageWSController
) : MessageService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Message {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<MessageDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<MessageDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<Message>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<MessageDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<Message>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: MessageRequest, replace: Boolean): MessageDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        val saved = mapper.toDto(repository.save(entity))
        val userSender = request.senderUuid?.let { userMapper.toDto(userService.getById(it)) }
        saved.sender = userSender
        messageWSController.sendMessage(request.receiverUuid!!, saved)
        return saved
    }

    @Transactional
    override fun saveMultiple(requestList: List<MessageRequest>): List<MessageDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: MessageRequest, includeDelete: Boolean): MessageDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        return mapper.toDto(repository.save(entity))
    }

    @Transactional
    override fun updateMultiple(dtoList: List<MessageDto>): List<MessageDto> {
        log.trace("updateMultiple -> dtoList: ${objectMapper.writeValueAsString(dtoList)}")
        val ids = dtoList.mapNotNull { it.uuid }
        val entities = repository.findAllById(ids)
        entities.forEach { entity ->
            val dto = dtoList.first { it.uuid == entity.uuid }
            mapper.update(mapper.toRequest(dto), entity)
        }
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun delete(id: UUID) {
        log.trace("delete -> id: $id")
        val entity = getById(id)
        entity.deleted = true
        entity.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        repository.save(entity)
    }

    @Transactional
    override fun deleteMultiple(idList: List<UUID>) {
        log.trace("deleteMultiple -> idList: $idList")
        val entities = repository.findAllById(idList)
        entities.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        }
        repository.saveAll(entities)
    }
}
