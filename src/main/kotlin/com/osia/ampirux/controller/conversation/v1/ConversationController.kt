package com.osia.ampirux.controller.conversation.v1

import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.conversation.v1.ConversationDto
import com.osia.ampirux.dto.conversation.v1.ConversationMapper
import com.osia.ampirux.dto.conversation.v1.ConversationRequest
import com.osia.ampirux.service.conversation.ConversationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("conversation.v1.crud")
@CrossOrigin
@RequestMapping("v1/conversations")
@Validated
class ConversationController(
    private val service: ConversationService,
    private val mapper: ConversationMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page<ConversationDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String,
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID): Page<ConversationDto> {
        return service.findAllByFilter(pageable, where, barbershopUuid)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ConversationDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ConversationDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody request: ConversationRequest): ResponseEntity<ConversationDto> {
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@RequestHeader("barbershop_uuid") barbershopUuid: UUID, @Validated(OnCreate::class) @RequestBody requestList: List<ConversationRequest>): ResponseEntity<List<ConversationDto>> {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: ConversationRequest): ResponseEntity<ConversationDto> {

        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List<ConversationDto>): ResponseEntity<List<ConversationDto>> {
        return ResponseEntity.ok(service.updateMultiple(dtoList))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<HttpStatus> {
        service.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(@RequestBody uuidList: List<UUID>): ResponseEntity<HttpStatus> {
        service.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get-my")
    fun getMy(
        @RequestHeader user: UUID,
        @RequestParam(defaultValue = "0") page: Int? = 0,
        @RequestParam(defaultValue = "20") size: Int? = 20
    ): ResponseEntity<Page<ConversationDto>> {
        return ResponseEntity.ok(service.getAllByUser(user, page ?: 0, size ?: 20))
    }

    @GetMapping("/get-conversation/{username}")
    fun getUserConversation(
        @RequestHeader user: UUID,
        @PathVariable username: String,
        @RequestParam(defaultValue = "0") page: Int? = 0,
        @RequestParam(defaultValue = "20") size: Int? = 20
    ): ResponseEntity<ConversationDto> {
        return ResponseEntity.ok(service.getUserConversation(user, username, page ?: 0, size ?: 20))
    }

    @PostMapping("/send-message/{uuid}")
    fun getUserConversation(@RequestHeader user: UUID, @PathVariable uuid: UUID, @RequestBody message: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(service.sendMessage(user, uuid, message))
    }

    @PostMapping("/read-messages/{uuid}")
    fun getUserConversation(@RequestHeader user: UUID, @PathVariable uuid: UUID): ResponseEntity<Boolean> {
        return ResponseEntity.ok(service.readMessages(user, uuid))
    }
}
