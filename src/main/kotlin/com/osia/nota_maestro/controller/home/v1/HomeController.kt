package com.osia.nota_maestro.controller.home.v1

import com.osia.nota_maestro.dto.home.v1.HomeAdminDto
import com.osia.nota_maestro.service.home.HomeService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("home.v1.crud")
@CrossOrigin
@RequestMapping("v1/home")
@Validated
class HomeController(
    private val homeService: HomeService,
) {
    @GetMapping("/admin")
    fun getById(@RequestHeader school: UUID): ResponseEntity<HomeAdminDto> {
        return ResponseEntity.ok().body(homeService.getByAdmin(school))
    }

    @GetMapping("/teacher/{uuid}")
    fun getByTeacher(@PathVariable uuid: UUID): ResponseEntity<HomeAdminDto> {
        return ResponseEntity.ok().body(homeService.getByTeacher(uuid))
    }

    @GetMapping("/student/{uuid}")
    fun getByStudent(@PathVariable uuid: UUID): ResponseEntity<HomeAdminDto> {
        return ResponseEntity.ok().body(homeService.getByStudent(uuid))
    }
}
