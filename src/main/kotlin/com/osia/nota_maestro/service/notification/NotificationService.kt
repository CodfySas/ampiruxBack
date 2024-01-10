package com.osia.nota_maestro.service.notification

import com.osia.nota_maestro.dto.notification.v1.NotificationDto
import com.osia.nota_maestro.dto.notification.v1.NotificationRequest
import com.osia.nota_maestro.model.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface NotificationService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Notification
    fun getByUser(uuid: UUID): List<NotificationDto>
    fun findByMultiple(uuidList: List<UUID>): List<NotificationDto>
    fun findAll(pageable: Pageable, school: UUID): Page<NotificationDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<NotificationDto>
    // Create
    fun save(notificationRequest: NotificationRequest, replace: Boolean = false): NotificationDto
    fun saveMultiple(notificationRequestList: List<NotificationRequest>): List<NotificationDto>
    // Update
    fun update(uuid: UUID, notificationRequest: NotificationRequest, includeDelete: Boolean = false): NotificationDto
    fun updateMultiple(notificationDtoList: List<NotificationDto>): List<NotificationDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
