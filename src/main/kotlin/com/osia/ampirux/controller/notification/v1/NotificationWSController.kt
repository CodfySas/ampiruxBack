package com.osia.ampirux.controller.notification.v1

import com.osia.ampirux.dto.notification.v1.NotificationDto
import com.osia.ampirux.dto.notification.v1.NotificationMessageDto
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@RestController("notificationWS.v1.crud")
@CrossOrigin
@RequestMapping("v1/ws-notifications")
@Validated
class NotificationWSController(private val simpMessagingAmpirux: SimpMessagingTemplate) {

    fun sendNotification(receiver: UUID, notification: NotificationDto) {
        val newNotification = NotificationMessageDto(
            notification.userUuid!!, notification.senderUuid, notification.type!!, notification.message, false,
            LocalDateTime.now(
                ZoneId.of("America/Bogota")
            ),
            notification.link, notification.userSender, notification.postUuid
        )
        simpMessagingAmpirux.convertAndSend("/topic/notifications/$receiver", newNotification)
    }
}
