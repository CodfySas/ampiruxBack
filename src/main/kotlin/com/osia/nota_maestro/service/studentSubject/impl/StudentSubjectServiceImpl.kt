package com.osia.nota_maestro.service.studentSubject.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectDto
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectMapper
import com.osia.nota_maestro.dto.studentSubject.v1.StudentSubjectRequest
import com.osia.nota_maestro.model.StudentSubject
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.service.studentSubject.StudentSubjectService
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

@Service("studentSubject.crud_service")
@Transactional
class StudentSubjectServiceImpl(
    private val studentSubjectRepository: StudentSubjectRepository,
    private val studentSubjectMapper: StudentSubjectMapper,
    private val objectMapper: ObjectMapper
) : StudentSubjectService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("studentSubject count -> increment: $increment")
        return studentSubjectRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): StudentSubject {
        return studentSubjectRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "StudentSubject $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<StudentSubjectDto> {
        log.trace("studentSubject findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return studentSubjectRepository.findAllById(uuidList).map(studentSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<StudentSubjectDto> {
        log.trace("studentSubject findAll -> pageable: $pageable")
        return studentSubjectRepository.findAll(Specification.where(CreateSpec<StudentSubject>().createSpec("", school)), pageable).map(studentSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentSubjectDto> {
        log.trace("studentSubject findAllByFilter -> pageable: $pageable, where: $where")
        return studentSubjectRepository.findAll(Specification.where(CreateSpec<StudentSubject>().createSpec(where, school)), pageable).map(studentSubjectMapper::toDto)
    }

    @Transactional
    override fun save(studentSubjectRequest: StudentSubjectRequest, replace: Boolean): StudentSubjectDto {
        log.trace("studentSubject save -> request: $studentSubjectRequest")
        val savedStudentSubject = studentSubjectMapper.toModel(studentSubjectRequest)
        return studentSubjectMapper.toDto(studentSubjectRepository.save(savedStudentSubject))
    }

    @Transactional
    override fun saveMultiple(studentSubjectRequestList: List<StudentSubjectRequest>): List<StudentSubjectDto> {
        log.trace("studentSubject saveMultiple -> requestList: ${objectMapper.writeValueAsString(studentSubjectRequestList)}")
        val studentSubjects = studentSubjectRequestList.map(studentSubjectMapper::toModel)
        return studentSubjectRepository.saveAll(studentSubjects).map(studentSubjectMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, studentSubjectRequest: StudentSubjectRequest): StudentSubjectDto {
        log.trace("studentSubject update -> uuid: $uuid, request: $studentSubjectRequest")
        val studentSubject = getById(uuid)
        studentSubjectMapper.update(studentSubjectRequest, studentSubject)
        return studentSubjectMapper.toDto(studentSubjectRepository.save(studentSubject))
    }

    @Transactional
    override fun updateMultiple(studentSubjectDtoList: List<StudentSubjectDto>): List<StudentSubjectDto> {
        log.trace("studentSubject updateMultiple -> studentSubjectDtoList: ${objectMapper.writeValueAsString(studentSubjectDtoList)}")
        val studentSubjects = studentSubjectRepository.findAllById(studentSubjectDtoList.mapNotNull { it.uuid })
        studentSubjects.forEach { studentSubject ->
            studentSubjectMapper.update(studentSubjectMapper.toRequest(studentSubjectDtoList.first { it.uuid == studentSubject.uuid }), studentSubject)
        }
        return studentSubjectRepository.saveAll(studentSubjects).map(studentSubjectMapper::toDto)
    }

    @Transactional
    override fun updateMultipleAndNullRecovery(studentSubjectDtoList: List<StudentSubjectDto>): List<StudentSubjectDto> {
        log.trace("studentSubject updateMultiple -> studentSubjectDtoList: ${objectMapper.writeValueAsString(studentSubjectDtoList)}")
        val studentSubjects = studentSubjectRepository.findAllById(studentSubjectDtoList.mapNotNull { it.uuid })
        studentSubjects.forEach { studentSubject ->
            val req = studentSubjectMapper.toRequest(studentSubjectDtoList.first { it.uuid == studentSubject.uuid })
            studentSubjectMapper.update(req, studentSubject)
            studentSubject.recovery = req.recovery
        }
        return studentSubjectRepository.saveAll(studentSubjects).map(studentSubjectMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("studentSubject delete -> uuid: $uuid")
        val studentSubject = getById(uuid)
        studentSubject.deleted = true
        studentSubject.deletedAt = LocalDateTime.now()
        studentSubjectRepository.save(studentSubject)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("studentSubject deleteMultiple -> uuid: $uuidList")
        val studentSubjects = studentSubjectRepository.findAllById(uuidList)
        studentSubjects.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        studentSubjectRepository.saveAll(studentSubjects)
    }
}
