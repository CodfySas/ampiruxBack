package com.osia.ampirux.controller.barbershop.v1

import com.osia.ampirux.dto.OnCreate
import com.osia.ampirux.dto.barbershop.v1.BarberShopDto
import com.osia.ampirux.dto.barbershop.v1.BarberShopMapper
import com.osia.ampirux.dto.barbershop.v1.BarberShopRequest
import com.osia.ampirux.dto.service.v1.ServiceDto
import com.osia.ampirux.service.barbershop.BarberShopService
import com.osia.ampirux.service.service.ServiceService
import com.osia.ampirux.service.user.UserService
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

@RestController("barbershop.v2.crud")
@CrossOrigin
@RequestMapping("barber-shops")
@Validated
class GetBarberShopController(
    private val service: BarberShopService,
    private val serviceService: ServiceService,
) {

    @GetMapping("/services")
    fun findAllByFilter(
        @RequestHeader("barbershop_uuid") barbershopUuid: UUID
    ): Page<ServiceDto> {
        return serviceService.findAllByFilter(Pageable.unpaged(), "-", barbershopUuid)
    }

    @GetMapping("/{code}")
    fun getByCode(@PathVariable code: String): ResponseEntity<BarberShopDto> {
        return ResponseEntity.ok(service.getByCode(code))
    }


}

