package com.osia.nota_maestro.controller.notification.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.notification.v1.NotificationDto
import com.osia.nota_maestro.dto.notification.v1.NotificationMapper
import com.osia.nota_maestro.dto.notification.v1.NotificationRequest
import com.osia.nota_maestro.service.notification.NotificationService
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
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("notification.v1.crud")
@CrossOrigin
@RequestMapping("v1/notifications")
@Validated
class NotificationController(
    private val notificationService: NotificationService,
    private val notificationMapper: NotificationMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<NotificationDto> {
        return notificationService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<NotificationDto> {
        return notificationService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return notificationService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<NotificationDto> {
        return ResponseEntity.ok().body(notificationMapper.toDto(notificationService.getById(uuid)))
    }

    @GetMapping("/my/{uuid}")
    fun getByUser(@PathVariable uuid: UUID): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity.ok().body(notificationService.getByUser(uuid))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity.ok().body(notificationService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: NotificationRequest,
        @RequestHeader school: UUID
    ): ResponseEntity<NotificationDto> {
        return ResponseEntity(notificationService.save(request, school), HttpStatus.CREATED)
    }

    @PostMapping("/role/{role}")
    fun generateToRole(
        @Validated(OnCreate::class) @RequestBody request: NotificationRequest,
        @RequestHeader school: UUID, @PathVariable role: String
    ): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity(notificationService.generateToRole(request, school, role), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody notificationRequestList: List<NotificationRequest>
    ): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity(notificationService.saveMultiple(notificationRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: NotificationRequest
    ): ResponseEntity<NotificationDto> {
        return ResponseEntity.ok().body(notificationService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody notificationDtoList: List<NotificationDto>
    ): ResponseEntity<List<NotificationDto>> {
        return ResponseEntity.ok().body(notificationService.updateMultiple(notificationDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        notificationService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        notificationService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
