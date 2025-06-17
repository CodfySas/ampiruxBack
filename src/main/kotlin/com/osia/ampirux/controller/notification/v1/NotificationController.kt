package com.osia.ampirux.controller.notification.v1

import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.notification.v1.NotificationDto
import com.osia.ampirux.dto.notification.v1.NotificationMapper
import com.osia.ampirux.dto.notification.v1.NotificationRequest
import com.osia.ampirux.service.notification.NotificationService
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

@RestController("notification.v1.crud")
@CrossOrigin
@RequestMapping("v1/notifications")
@Validated
class NotificationController(
    private val service: NotificationService,
    private val mapper: NotificationMapper
) {
    @GetMapping
    fun findAll(pageable: Pageable): Page<NotificationDto> {
        return service.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<NotificationDto> {
        return service.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return service.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<NotificationDto> {
        return ResponseEntity.ok(mapper.toDto(service.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity.ok(service.findByMultiple(uuidList))
    }

    @PostMapping
    fun save(@Validated(OnCreate::class) @RequestBody request: NotificationRequest, @RequestHeader user: UUID): ResponseEntity<NotificationDto> {
        request.userUuid = user
        return ResponseEntity(service.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(@Validated(OnCreate::class) requestList: List<NotificationRequest>): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity(service.saveMultiple(requestList), HttpStatus.CREATED)
    }

    @PatchMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID, @RequestBody request: NotificationRequest): ResponseEntity<NotificationDto> {
        return ResponseEntity.ok(service.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(@RequestBody dtoList: List<NotificationDto>): ResponseEntity<List<NotificationDto>> {
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

    @GetMapping("/get-notifications/get")
    fun getByUserUuid(
        @RequestHeader user: UUID,
        @RequestParam(defaultValue = "0") page: Int? = 0,
        @RequestParam(defaultValue = "20") size: Int? = 20
    ): ResponseEntity<Page<NotificationDto>> {

        return ResponseEntity.ok(service.getByUser(user, page ?: 0, size ?: 20))
    }

    @PostMapping("/read-notifications")
    fun markAsRead(@RequestHeader user: UUID): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity.ok(service.markAsRead(user))
    }
}
