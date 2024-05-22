package com.osia.nota_maestro.service.diagnosis.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisCompleteDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisDto
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisMapper
import com.osia.nota_maestro.dto.diagnosis.v1.DiagnosisRequest
import com.osia.nota_maestro.dto.diagnosis.v1.UserDiagnosesDto
import com.osia.nota_maestro.model.Diagnosis
import com.osia.nota_maestro.repository.diagnosis.DiagnosisRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.diagnosis.DiagnosisService
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

@Service("diagnosis.crud_service")
@Transactional
class DiagnosisServiceImpl(
    private val diagnosisRepository: DiagnosisRepository,
    private val diagnosisMapper: DiagnosisMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val gradeRepository: GradeRepository
) : DiagnosisService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("diagnosis count -> increment: $increment")
        return diagnosisRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Diagnosis {
        return diagnosisRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Diagnosis $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<DiagnosisDto> {
        log.trace("diagnosis findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return diagnosisRepository.findAllById(uuidList).map(diagnosisMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<DiagnosisDto> {
        log.trace("diagnosis findAll -> pageable: $pageable")
        return diagnosisRepository.findAll(Specification.where(CreateSpec<Diagnosis>().createSpec("", school)), pageable).map(diagnosisMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DiagnosisDto> {
        log.trace("diagnosis findAllByFilter -> pageable: $pageable, where: $where")
        return diagnosisRepository.findAll(Specification.where(CreateSpec<Diagnosis>().createSpec(where, school)), pageable).map(diagnosisMapper::toDto)
    }

    @Transactional
    override fun save(diagnosisRequest: DiagnosisRequest, replace: Boolean): DiagnosisDto {
        log.trace("diagnosis save -> request: $diagnosisRequest")
        val savedDiagnosis = diagnosisMapper.toModel(diagnosisRequest)
        return diagnosisMapper.toDto(diagnosisRepository.save(savedDiagnosis))
    }

    @Transactional
    override fun saveMultiple(diagnosisRequestList: List<DiagnosisRequest>): List<DiagnosisDto> {
        log.trace("diagnosis saveMultiple -> requestList: ${objectMapper.writeValueAsString(diagnosisRequestList)}")
        val diagnoses = diagnosisRequestList.map(diagnosisMapper::toModel)
        return diagnosisRepository.saveAll(diagnoses).map(diagnosisMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, diagnosisRequest: DiagnosisRequest, includeDelete: Boolean): DiagnosisDto {
        log.trace("diagnosis update -> uuid: $uuid, request: $diagnosisRequest")
        val diagnosis = if (!includeDelete) {
            getById(uuid)
        } else {
            diagnosisRepository.getByUuid(uuid).get()
        }
        diagnosisMapper.update(diagnosisRequest, diagnosis)
        return diagnosisMapper.toDto(diagnosisRepository.save(diagnosis))
    }

    @Transactional
    override fun updateMultiple(diagnosisDtoList: List<DiagnosisDto>): List<DiagnosisDto> {
        log.trace("diagnosis updateMultiple -> diagnosisDtoList: ${objectMapper.writeValueAsString(diagnosisDtoList)}")
        val diagnoses = diagnosisRepository.findAllById(diagnosisDtoList.mapNotNull { it.uuid })
        diagnoses.forEach { diagnosis ->
            diagnosisMapper.update(diagnosisMapper.toRequest(diagnosisDtoList.first { it.uuid == diagnosis.uuid }), diagnosis)
        }
        return diagnosisRepository.saveAll(diagnoses).map(diagnosisMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("diagnosis delete -> uuid: $uuid")
        val diagnosis = getById(uuid)
        diagnosis.deleted = true
        diagnosis.deletedAt = LocalDateTime.now()
        diagnosisRepository.save(diagnosis)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("diagnosis deleteMultiple -> uuid: $uuidList")
        val diagnoses = diagnosisRepository.findAllById(uuidList)
        diagnoses.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        diagnosisRepository.saveAll(diagnoses)
    }

    override fun getComplete(school: UUID): DiagnosisCompleteDto {
        log.trace("diagnosis getComplete -> uuid: $school")
        val diagnoses = diagnosisRepository.findAllByUuidSchool(school).map(diagnosisMapper::toDto).sortedBy { it.code }
        val students = userRepository.findAllByRoleAndUuidSchool("student", school)
        val grades = gradeRepository.findAllByUuidSchool(school)
        return DiagnosisCompleteDto().apply {
            this.diagnoses = diagnoses
            this.users = students.map { s ->
                UserDiagnosesDto().apply {
                    this.uuid = s.uuid
                    this.age = null
                    this.grade = grades.firstOrNull { g -> g.uuid == s.actualGrade }?.name
                    this.name = s.name
                    this.lastname = s.lastname
                }
            }
        }
    }

    override fun submitComplete(req: List<DiagnosisDto>, school: UUID): DiagnosisCompleteDto {
        log.trace("diagnosis submitComplete -> uuid: $school")
        val diagnoses = diagnosisRepository.findAllByUuidSchool(school).map(diagnosisMapper::toDto).sortedBy { it.code }
        val toCreate = req.filter { r -> r.uuid == null }
        val toUpdate = req.filter { r -> r.uuid != null }
        val toDelete = diagnoses.filterNot { d -> toUpdate.mapNotNull { it.uuid }.contains(d.uuid) }

        req.forEach {
            it.uuidSchool = school
        }

        this.saveMultiple(toCreate.map(diagnosisMapper::toRequest))
        this.updateMultiple(toUpdate)
        this.deleteMultiple(toDelete.mapNotNull { it.uuid })

        return DiagnosisCompleteDto()
    }
}
