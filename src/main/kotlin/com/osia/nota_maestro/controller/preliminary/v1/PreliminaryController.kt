package com.osia.nota_maestro.controller.preliminary.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllRequest
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryMapper
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryRequest
import com.osia.nota_maestro.service.preliminary.PreliminaryService
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

@RestController("preliminary.v1.crud")
@CrossOrigin
@RequestMapping("v1/preliminaries")
@Validated
class PreliminaryController(
    private val preliminaryService: PreliminaryService,
    private val preliminaryMapper: PreliminaryMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<PreliminaryDto> {
        return preliminaryService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<PreliminaryDto> {
        return preliminaryService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return preliminaryService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<PreliminaryDto> {
        return ResponseEntity.ok().body(preliminaryMapper.toDto(preliminaryService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<PreliminaryDto>> {
        return ResponseEntity.ok().body(preliminaryService.findByMultiple(uuidList))
    }

    @PostMapping("/by-classroom")
    fun getByAll(@RequestBody preliminaryReq: PreliminaryAllRequest, @RequestHeader user: UUID): ResponseEntity<List<PreliminaryAllDto>>{
        return ResponseEntity.ok().body(preliminaryService.getByClassroom(preliminaryReq, user))
    }

    @PostMapping("/submit/{classroom}/{period}")
    fun submit(@RequestBody preliminaryReq: List<PreliminaryAllDto>, @PathVariable classroom: UUID, @PathVariable period: Int): ResponseEntity<List<PreliminaryAllDto>>{
        return ResponseEntity.ok().body(preliminaryService.submit(preliminaryReq, period, classroom))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: PreliminaryRequest
    ): ResponseEntity<PreliminaryDto> {
        return ResponseEntity(preliminaryService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody preliminaryRequestList: List<PreliminaryRequest>
    ): ResponseEntity<List<PreliminaryDto>> {
        return ResponseEntity(preliminaryService.saveMultiple(preliminaryRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PreliminaryRequest
    ): ResponseEntity<PreliminaryDto> {
        return ResponseEntity.ok().body(preliminaryService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody preliminaryDtoList: List<PreliminaryDto>
    ): ResponseEntity<List<PreliminaryDto>> {
        return ResponseEntity.ok().body(preliminaryService.updateMultiple(preliminaryDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        preliminaryService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        preliminaryService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
