package com.osia.nota_maestro.service.certificate

import com.osia.nota_maestro.dto.certificate.v1.CertificateDto
import com.osia.nota_maestro.dto.certificate.v1.CertificateRequest
import com.osia.nota_maestro.model.Certificate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CertificateService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Certificate
    fun findByMultiple(uuidList: List<UUID>): List<CertificateDto>
    fun findAll(pageable: Pageable, school: UUID): Page<CertificateDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<CertificateDto>
    // Create
    fun save(certificateRequest: CertificateRequest, replace: Boolean = false): CertificateDto
    fun saveMultiple(certificateRequestList: List<CertificateRequest>): List<CertificateDto>
    // Update
    fun update(uuid: UUID, certificateRequest: CertificateRequest, includeDelete: Boolean = false): CertificateDto
    fun updateMultiple(certificateDtoList: List<CertificateDto>): List<CertificateDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getMy(user: UUID): List<CertificateDto>
    fun getRequested(school: UUID): List<CertificateDto>
    fun request(type: String, user: UUID, school: UUID): CertificateDto
}
