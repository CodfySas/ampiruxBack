package com.osia.nota_maestro.service.certificate.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.certificate.v1.CertificateDto
import com.osia.nota_maestro.dto.certificate.v1.CertificateMapper
import com.osia.nota_maestro.dto.certificate.v1.CertificateRequest
import com.osia.nota_maestro.model.Certificate
import com.osia.nota_maestro.repository.certificate.CertificateRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.certificate.CertificateService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID

@Service("certificate.crud_service")
@Transactional
class CertificateServiceImpl(
    private val certificateRepository: CertificateRepository,
    private val certificateMapper: CertificateMapper,
    private val objectMapper: ObjectMapper,
    private val schoolService: SchoolService,
    private val userRepository: UserRepository
) : CertificateService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("certificate count -> increment: $increment")
        return certificateRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Certificate {
        return certificateRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Certificate $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<CertificateDto> {
        log.trace("certificate findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return certificateRepository.findAllById(uuidList).map(certificateMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<CertificateDto> {
        log.trace("certificate findAll -> pageable: $pageable")
        return certificateRepository.findAll(Specification.where(CreateSpec<Certificate>().createSpec("", school)), pageable).map(certificateMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<CertificateDto> {
        log.trace("certificate findAllByFilter -> pageable: $pageable, where: $where")
        return certificateRepository.findAll(Specification.where(CreateSpec<Certificate>().createSpec(where, school)), pageable).map(certificateMapper::toDto)
    }

    @Transactional
    override fun save(certificateRequest: CertificateRequest, replace: Boolean): CertificateDto {
        log.trace("certificate save -> request: $certificateRequest")
        val savedCertificate = certificateMapper.toModel(certificateRequest)
        return certificateMapper.toDto(certificateRepository.save(savedCertificate))
    }

    @Transactional
    override fun saveMultiple(certificateRequestList: List<CertificateRequest>): List<CertificateDto> {
        log.trace("certificate saveMultiple -> requestList: ${objectMapper.writeValueAsString(certificateRequestList)}")
        val certificates = certificateRequestList.map(certificateMapper::toModel)
        return certificateRepository.saveAll(certificates).map(certificateMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, certificateRequest: CertificateRequest, includeDelete: Boolean): CertificateDto {
        log.trace("certificate update -> uuid: $uuid, request: $certificateRequest")
        val certificate = if (!includeDelete) {
            getById(uuid)
        } else {
            certificateRepository.getByUuid(uuid).get()
        }
        certificateMapper.update(certificateRequest, certificate)
        if (certificateRequest.status == "completed") {
            certificate.approvedAt = LocalDateTime.now()
        }
        return certificateMapper.toDto(certificateRepository.save(certificate))
    }

    @Transactional
    override fun updateMultiple(certificateDtoList: List<CertificateDto>): List<CertificateDto> {
        log.trace("certificate updateMultiple -> certificateDtoList: ${objectMapper.writeValueAsString(certificateDtoList)}")
        val certificates = certificateRepository.findAllById(certificateDtoList.mapNotNull { it.uuid })
        certificates.forEach { certificate ->
            certificateMapper.update(certificateMapper.toRequest(certificateDtoList.first { it.uuid == certificate.uuid }), certificate)
        }
        return certificateRepository.saveAll(certificates).map(certificateMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("certificate delete -> uuid: $uuid")
        val certificate = getById(uuid)
        certificate.deleted = true
        certificate.deletedAt = LocalDateTime.now()
        certificateRepository.save(certificate)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("certificate deleteMultiple -> uuid: $uuidList")
        val certificates = certificateRepository.findAllById(uuidList)
        certificates.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        certificateRepository.saveAll(certificates)
    }

    override fun getMy(user: UUID): List<CertificateDto> {
        log.trace("certificate getMy -> uuid: $user")
        val my = certificateRepository.findAllByUuidUser(user)
        return my.map(certificateMapper::toDto)
    }

    override fun getRequested(school: UUID): List<CertificateDto> {
        log.trace("certificate getRequested -> uuid: $school")
        val certificates = certificateRepository.findAllByUuidSchoolAndStatus(school, "pending").map(certificateMapper::toDto)
        val users = userRepository.findAllById(certificates.mapNotNull { it.uuidUser }.distinct())
        certificates.forEach {
            val my = users.firstOrNull { u -> u.uuid == it.uuidUser }
            it.user = my?.name + " " + my?.lastname
            it.role = when (my?.role) {
                "admin" -> "Administrador"
                "teacher" -> "Docente"
                "student" -> "Estudiante"
                else -> ""
            }
        }
        return certificates
    }

    override fun request(type: String, user: UUID, school: UUID): CertificateDto {
        return save(
            CertificateRequest().apply {
                this.type = type
                this.uuidSchool = school
                this.approved = false
                this.uuidUser = user
                this.status = "pending"
            }
        )
    }
}
