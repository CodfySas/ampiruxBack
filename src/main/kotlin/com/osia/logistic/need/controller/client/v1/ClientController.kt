package com.osia.logistic.need.controller.client.v1

import com.osia.logistic.need.dto.client.v1.ClientDto
import com.osia.logistic.need.dto.client.v1.ClientRequest
import com.osia.logistic.need.dto.request.OnCreate
import com.osia.logistic.need.model.Client
import com.osia.logistic.need.service.client.ClientService
import com.sipios.springsearch.anotation.SearchSpec
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
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
@Api(tags = ["clients", "crud"])
@Validated
class ClientController(
    private val clientService: ClientService,
) {

    @GetMapping
    fun index(pageable: Pageable, @SearchSpec specs: Specification<Client>?): Page<ClientDto> {
        return clientService.findAll(pageable, specs)
    }

    @GetMapping("/{uuid}")
    fun show(@PathVariable uuid: UUID): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.findByUuid(uuid))
    }

    @PostMapping
    fun create(
        @Validated(OnCreate::class) @RequestBody request: ClientRequest
    ): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.save(request))
    }

    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClientRequest
    ): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.update(uuid, request))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<ClientDto> {
        return ResponseEntity.ok(clientService.delete(uuid))
    }

    @PostMapping("/multiple")
    fun findByMany(@RequestBody clientList: List<UUID>): List<ClientDto> {
        return clientService.findByUuidIn(clientList)
    }
}
