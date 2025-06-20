package com.osia.ampirux.service.notification.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.ampirux.controller.notification.v1.NotificationWSController
import com.osia.ampirux.dto.notification.v1.NotificationDto
import com.osia.ampirux.dto.notification.v1.NotificationMapper
import com.osia.ampirux.dto.notification.v1.NotificationRequest
import com.osia.ampirux.dto.user.v1.UserMapper
import com.osia.ampirux.model.Notification
import com.osia.ampirux.model.enums.NotificationEnum
import com.osia.ampirux.repository.notification.NotificationRepository
import com.osia.ampirux.service.notification.NotificationService
import com.osia.ampirux.service.user.UserService
import com.osia.ampirux.util.CreateSpec
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Optional
import java.util.UUID

@Transactional
@Service
class NotificationServiceImpl(
    private val repository: NotificationRepository,
    private val mapper: NotificationMapper,
    private val objectMapper: ObjectMapper,
    private val notificationWSController: NotificationWSController,
    private val userService: UserService,
    private val userMapper: UserMapper
) : NotificationService {

    protected val log: Logger = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("count -> increment: $increment")
        return repository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(id: UUID): Notification {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Entity $id not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(idList: List<UUID>): List<NotificationDto> {
        log.trace("findByMultiple -> idList: ${objectMapper.writeValueAsString(idList)}")
        return repository.findAllById(idList).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<NotificationDto> {
        log.trace("findAll -> pageable: $pageable")
        return repository.findAll(Specification.where(CreateSpec<Notification>().createSpec("")), pageable).map(mapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, barberShopUuid: UUID): Page<NotificationDto> {
        log.trace("findAllByFilter -> pageable: $pageable, where: $where")
        return repository.findAll(Specification.where(CreateSpec<Notification>().createSpec(where)), pageable).map(mapper::toDto)
    }

    @Transactional
    override fun save(request: NotificationRequest, replace: Boolean): NotificationDto {
        log.trace("save -> request: $request")
        val entity = mapper.toModel(request)
        val saved = mapper.toDto(repository.save(entity))
        val userSender = request.senderUuid?.let { userMapper.toDto(userService.getById(it)) }
        saved.userSender = userSender
        notificationWSController.sendNotification(request.userUuid!!, saved)
        return saved
    }

    @Transactional
    override fun saveMultiple(requestList: List<NotificationRequest>): List<NotificationDto> {
        log.trace("saveMultiple -> requestList: ${objectMapper.writeValueAsString(requestList)}")
        val entities = requestList.map(mapper::toModel)
        return repository.saveAll(entities).map(mapper::toDto)
    }

    @Transactional
    override fun update(id: UUID, request: NotificationRequest, includeDelete: Boolean): NotificationDto {
        log.trace("update -> id: $id, request: $request")
        val entity = if (!includeDelete) {
            getById(id)
        } else {
            repository.getByUuid(id).get()
        }
        mapper.update(request, entity)
        val saved = mapper.toDto(repository.save(entity))
        val userSender = request.senderUuid?.let { userMapper.toDto(userService.getById(it)) }
        saved.userSender = userSender
        notificationWSController.sendNotification(request.userUuid!!, saved)
        return saved
    }

    @Transactional
    override fun updateMultiple(dtoList: List<NotificationDto>): List<NotificationDto> {
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

    override fun getByUser(user: UUID, page: Int, size: Int): Page<NotificationDto> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val notifications = repository.findAllByUserUuid(user, pageable).map(mapper::toDto)
        val userSenders = userService.findByMultiple(notifications.mapNotNull { it.senderUuid })
        notifications.forEach { n ->
            n.userSender = userSenders.firstOrNull { u -> u.uuid == n.senderUuid }
        }
        return notifications
    }

    @Transactional
    override fun markAsRead(user: UUID): List<NotificationDto> {
        val notifications = repository.getAllByUserUuid(user)
        notifications.forEach { n ->
            n.read = true
        }
        repository.saveAll(notifications)
        return notifications.map(mapper::toDto)
    }

    override fun getByPost(postUuid: UUID, type: NotificationEnum): Optional<Notification> {
        return repository.getFirstByPostUuidAndType(postUuid, type)
    }
}
