package com.osia.nota_maestro.service.notification.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.notification.v1.NotificationDto
import com.osia.nota_maestro.dto.notification.v1.NotificationMapper
import com.osia.nota_maestro.dto.notification.v1.NotificationRequest
import com.osia.nota_maestro.model.Notification
import com.osia.nota_maestro.repository.notification.NotificationRepository
import com.osia.nota_maestro.service.notification.NotificationService
import com.osia.nota_maestro.util.CreateSpec
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

@Service("notification.crud_service")
@Transactional
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val notificationMapper: NotificationMapper,
    private val objectMapper: ObjectMapper
) : NotificationService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("notification count -> increment: $increment")
        return notificationRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Notification {
        return notificationRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Notification $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun getByUser(uuid: UUID): List<NotificationDto> {
        return notificationRepository.findAllByUuidUser(uuid).map(notificationMapper::toDto).sortedByDescending { it.datetime }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<NotificationDto> {
        log.trace("notification findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return notificationRepository.findAllById(uuidList).map(notificationMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<NotificationDto> {
        log.trace("notification findAll -> pageable: $pageable")
        return notificationRepository.findAll(Specification.where(CreateSpec<Notification>().createSpec("", school)), pageable).map(notificationMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<NotificationDto> {
        log.trace("notification findAllByFilter -> pageable: $pageable, where: $where")
        return notificationRepository.findAll(Specification.where(CreateSpec<Notification>().createSpec(where, school)), pageable).map(notificationMapper::toDto)
    }

    @Transactional
    override fun save(notificationRequest: NotificationRequest, school: UUID, replace: Boolean): NotificationDto {
        log.trace("notification save -> request: $notificationRequest")
        val savedNotification = notificationMapper.toModel(notificationRequest)
        savedNotification.datetime = LocalDateTime.now()
        savedNotification.uuidSchool = school
        return notificationMapper.toDto(notificationRepository.save(savedNotification))
    }

    @Transactional
    override fun saveMultiple(notificationRequestList: List<NotificationRequest>): List<NotificationDto> {
        log.trace("notification saveMultiple -> requestList: ${objectMapper.writeValueAsString(notificationRequestList)}")
        val notifications = notificationRequestList.map(notificationMapper::toModel)
        return notificationRepository.saveAll(notifications).map(notificationMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, notificationRequest: NotificationRequest, includeDelete: Boolean): NotificationDto {
        log.trace("notification update -> uuid: $uuid, request: $notificationRequest")
        val notification = if (!includeDelete) {
            getById(uuid)
        } else {
            notificationRepository.getByUuid(uuid).get()
        }
        notificationMapper.update(notificationRequest, notification)
        return notificationMapper.toDto(notificationRepository.save(notification))
    }

    @Transactional
    override fun updateMultiple(notificationDtoList: List<NotificationDto>): List<NotificationDto> {
        log.trace("notification updateMultiple -> notificationDtoList: ${objectMapper.writeValueAsString(notificationDtoList)}")
        val notifications = notificationRepository.findAllById(notificationDtoList.mapNotNull { it.uuid })
        notifications.forEach { notification ->
            notificationMapper.update(notificationMapper.toRequest(notificationDtoList.first { it.uuid == notification.uuid }), notification)
        }
        return notificationRepository.saveAll(notifications).map(notificationMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("notification delete -> uuid: $uuid")
        val notification = getById(uuid)
        notification.deleted = true
        notification.deletedAt = LocalDateTime.now()
        notificationRepository.save(notification)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("notification deleteMultiple -> uuid: $uuidList")
        val notifications = notificationRepository.findAllById(uuidList)
        notifications.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        notificationRepository.saveAll(notifications)
    }
}
