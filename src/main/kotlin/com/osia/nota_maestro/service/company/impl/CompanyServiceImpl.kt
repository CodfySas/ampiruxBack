package com.osia.nota_maestro.service.company.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.company.v1.CompanyDto
import com.osia.nota_maestro.dto.company.v1.CompanyMapper
import com.osia.nota_maestro.dto.company.v1.CompanyRequest
import com.osia.nota_maestro.model.Company
import com.osia.nota_maestro.repository.company.CompanyRepository
import com.osia.nota_maestro.service.company.CompanyService
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
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("company.crud_service")
@Transactional
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository,
    private val companyMapper: CompanyMapper,
    private val objectMapper: ObjectMapper
) : CompanyService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("company count -> increment: $increment")
        return companyRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Company {
        return companyRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Company $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<CompanyDto> {
        log.trace("company findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return companyRepository.findAllById(uuidList).map(companyMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<CompanyDto> {
        log.trace("company findAll -> pageable: $pageable")
        return companyRepository.findAll(pageable).map(companyMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<CompanyDto> {
        log.trace("company findAllByFilter -> pageable: $pageable, where: $where")
        return companyRepository.findAll(Specification.where(createSpec(where)), pageable).map(companyMapper::toDto)
    }

    @Transactional
    override fun save(companyRequest: CompanyRequest): CompanyDto {
        log.trace("company save -> request: $companyRequest")
        val company = companyMapper.toModel(companyRequest)
        return companyMapper.toDto(companyRepository.save(company))
    }

    @Transactional
    override fun saveMultiple(companyRequestList: List<CompanyRequest>): List<CompanyDto> {
        log.trace("company saveMultiple -> requestList: ${objectMapper.writeValueAsString(companyRequestList)}")
        val companies = companyRequestList.map(companyMapper::toModel)
        return companyRepository.saveAll(companies).map(companyMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, companyRequest: CompanyRequest): CompanyDto {
        log.trace("company update -> uuid: $uuid, request: $companyRequest")
        val company = getById(uuid)
        companyMapper.update(companyRequest, company)
        return companyMapper.toDto(companyRepository.save(company))
    }

    @Transactional
    override fun updateMultiple(companyDtoList: List<CompanyDto>): List<CompanyDto> {
        log.trace("company updateMultiple -> companyDtoList: ${objectMapper.writeValueAsString(companyDtoList)}")
        val companies = companyRepository.findAllById(companyDtoList.mapNotNull { it.uuid })
        companies.forEach { company ->
            companyMapper.update(companyMapper.toRequest(companyDtoList.first { it.uuid == company.uuid }), company)
        }
        return companyRepository.saveAll(companies).map(companyMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("company delete -> uuid: $uuid")
        val company = getById(uuid)
        company.deleted = true
        company.deletedAt = LocalDateTime.now()
        companyRepository.save(company)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("company deleteMultiple -> uuid: $uuidList")
        val companies = companyRepository.findAllById(uuidList)
        companies.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        companyRepository.saveAll(companies)
    }

    fun createSpec(where: String): Specification<Company> {
        var finalSpec = Specification { root: Root<Company>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<Company>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
