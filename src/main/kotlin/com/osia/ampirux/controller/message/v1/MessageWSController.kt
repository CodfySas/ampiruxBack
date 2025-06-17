package com.osia.ampirux.controller.message.v1

import com.osia.ampirux.dto.message.v1.MessageDto
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("messageWS.v1.crud")
@CrossOrigin
@RequestMapping("v1/ws-messages")
@Validated
class MessageWSController(private val simpMessagingAmpirux: SimpMessagingTemplate) {

    fun sendMessage(receiver: UUID, message: MessageDto) {
        simpMessagingAmpirux.convertAndSend("/topic/messages/$receiver", message)
    }
}
