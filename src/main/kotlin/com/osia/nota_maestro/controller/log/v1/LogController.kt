package com.osia.nota_maestro.controller.log.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.log.v1.LogDto
import com.osia.nota_maestro.dto.log.v1.LogMapper
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.service.log.LogService
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

@RestController("log.v1.crud")
@CrossOrigin
@RequestMapping("v1/logs")
@Validated
class LogController(
    private val logService: LogService,
    private val logMapper: LogMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<LogDto> {
        return logService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<LogDto> {
        return logService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return logService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<LogDto> {
        return ResponseEntity.ok().body(logMapper.toDto(logService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<LogDto>> {
        return ResponseEntity.ok().body(logService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: LogRequest
    ): ResponseEntity<LogDto> {
        return ResponseEntity(logService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody logRequestList: List<LogRequest>
    ): ResponseEntity<List<LogDto>> {
        return ResponseEntity(logService.saveMultiple(logRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: LogRequest
    ): ResponseEntity<LogDto> {
        return ResponseEntity.ok().body(logService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody logDtoList: List<LogDto>
    ): ResponseEntity<List<LogDto>> {
        return ResponseEntity.ok().body(logService.updateMultiple(logDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        logService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        logService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get-complete/{month}/{day}")
    fun getComplete(pageable: Pageable, @PathVariable month: Int, @PathVariable day: Int, @RequestHeader school: UUID): ResponseEntity<List<LogDto>> {
        return ResponseEntity.ok(logService.findAllByMonth(pageable, month, day, school))
    }
}
