package com.osia.nota_maestro.controller.certificate.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.certificate.v1.CertificateDto
import com.osia.nota_maestro.dto.certificate.v1.CertificateMapper
import com.osia.nota_maestro.dto.certificate.v1.CertificateRequest
import com.osia.nota_maestro.service.certificate.CertificateService
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

@RestController("certificate.v1.crud")
@CrossOrigin
@RequestMapping("v1/certificates")
@Validated
class CertificateController(
    private val certificateService: CertificateService,
    private val certificateMapper: CertificateMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<CertificateDto> {
        return certificateService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<CertificateDto> {
        return certificateService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return certificateService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<CertificateDto> {
        return ResponseEntity.ok().body(certificateMapper.toDto(certificateService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<CertificateDto>> {
        return ResponseEntity.ok().body(certificateService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: CertificateRequest
    ): ResponseEntity<CertificateDto> {
        return ResponseEntity(certificateService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody certificateRequestList: List<CertificateRequest>
    ): ResponseEntity<List<CertificateDto>> {
        return ResponseEntity(certificateService.saveMultiple(certificateRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: CertificateRequest
    ): ResponseEntity<CertificateDto> {
        return ResponseEntity.ok().body(certificateService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody certificateDtoList: List<CertificateDto>
    ): ResponseEntity<List<CertificateDto>> {
        return ResponseEntity.ok().body(certificateService.updateMultiple(certificateDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        certificateService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        certificateService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/my/{user}")
    fun getMy(@PathVariable user: UUID): ResponseEntity<List<CertificateDto>> {
        return ResponseEntity.ok(certificateService.getMy(user))
    }

    @GetMapping("/requested")
    fun getRequested(@RequestHeader school: UUID): ResponseEntity<List<CertificateDto>> {
        return ResponseEntity.ok(certificateService.getRequested(school))
    }

    @PostMapping("/request/{type}/{user}")
    fun requestCertificate(@PathVariable type: String, @PathVariable user: UUID, @RequestHeader school: UUID): ResponseEntity<CertificateDto> {
        return ResponseEntity.ok(certificateService.request(type, user, school))
    }
}
