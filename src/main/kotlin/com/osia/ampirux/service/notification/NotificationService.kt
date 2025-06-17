package com.osia.ampirux.service.notification

import com.osia.ampirux.dto.notification.v1.NotificationDto
import com.osia.ampirux.dto.notification.v1.NotificationRequest
import com.osia.ampirux.model.Notification
import com.osia.ampirux.model.enums.NotificationEnum
import com.osia.ampirux.service.common.CommonService
import org.springframework.data.domain.Page
import java.util.Optional
import java.util.UUID

interface NotificationService : CommonService<Notification, NotificationDto, NotificationRequest> {
    fun getByUser(user: UUID, page: Int, size: Int): Page<NotificationDto>
    fun markAsRead(user: UUID): List<NotificationDto>
    fun getByPost(postUuid: UUID, type: NotificationEnum): Optional<Notification>
}
