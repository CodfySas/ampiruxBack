package com.osia.osia_erp.controller.client.v1

import com.osia.osia_erp.dto.OnCreate
import com.osia.osia_erp.dto.client.v1.ClientDto
import com.osia.osia_erp.dto.client.v1.ClientMapper
import com.osia.osia_erp.dto.client.v1.ClientRequest
import com.osia.osia_erp.service.client.ClientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("client.v1.crud")
@RequestMapping("v1/clients")
@Validated
class ClientController(
    private val clientService: ClientService,
    private val clientMapper: ClientMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable): Page<ClientDto> {
        return clientService.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<ClientDto> {
        return clientService.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return clientService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ClientDto> {
        return ResponseEntity.ok().body(clientMapper.toDto(clientService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ClientDto>> {
        return ResponseEntity.ok().body(clientService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ClientRequest
    ): ResponseEntity<ClientDto> {
        return ResponseEntity(clientService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody clientRequestList: List<ClientRequest>
    ): ResponseEntity<List<ClientDto>> {
        return ResponseEntity(clientService.saveMultiple(clientRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClientRequest
    ): ResponseEntity<ClientDto> {
        return ResponseEntity.ok().body(clientService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody clientDtoList: List<ClientDto>
    ): ResponseEntity<List<ClientDto>> {
        return ResponseEntity.ok().body(clientService.updateMultiple(clientDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        clientService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        clientService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
