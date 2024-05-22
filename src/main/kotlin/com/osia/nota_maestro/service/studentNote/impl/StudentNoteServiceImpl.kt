package com.osia.nota_maestro.service.studentNote.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteDto
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteMapper
import com.osia.nota_maestro.dto.studentNote.v1.StudentNoteRequest
import com.osia.nota_maestro.model.StudentNote
import com.osia.nota_maestro.repository.studentNote.StudentNoteRepository
import com.osia.nota_maestro.service.studentNote.StudentNoteService
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

@Service("studentNote.crud_service")
@Transactional
class StudentNoteServiceImpl(
    private val studentNoteRepository: StudentNoteRepository,
    private val studentNoteMapper: StudentNoteMapper,
    private val objectMapper: ObjectMapper
) : StudentNoteService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("studentNote count -> increment: $increment")
        return studentNoteRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): StudentNote {
        return studentNoteRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "StudentNote $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<StudentNoteDto> {
        log.trace("studentNote findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return studentNoteRepository.findAllById(uuidList).map(studentNoteMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<StudentNoteDto> {
        log.trace("studentNote findAll -> pageable: $pageable")
        return studentNoteRepository.findAll(Specification.where(CreateSpec<StudentNote>().createSpec("", school)), pageable).map(studentNoteMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentNoteDto> {
        log.trace("studentNote findAllByFilter -> pageable: $pageable, where: $where")
        return studentNoteRepository.findAll(Specification.where(CreateSpec<StudentNote>().createSpec(where, school)), pageable).map(studentNoteMapper::toDto)
    }

    @Transactional
    override fun save(studentNoteRequest: StudentNoteRequest, replace: Boolean): StudentNoteDto {
        log.trace("studentNote save -> request: $studentNoteRequest")
        val savedStudentNote = studentNoteMapper.toModel(studentNoteRequest)
        return studentNoteMapper.toDto(studentNoteRepository.save(savedStudentNote))
    }

    @Transactional
    override fun saveMultiple(studentNoteRequestList: List<StudentNoteRequest>): List<StudentNoteDto> {
        log.trace("studentNote saveMultiple -> requestList: ${objectMapper.writeValueAsString(studentNoteRequestList)}")
        val studentNotes = studentNoteRequestList.map(studentNoteMapper::toModel)
        return studentNoteRepository.saveAll(studentNotes).map(studentNoteMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, studentNoteRequest: StudentNoteRequest, includeDelete: Boolean): StudentNoteDto {
        log.trace("studentNote update -> uuid: $uuid, request: $studentNoteRequest")
        val studentNote = if (!includeDelete) {
            getById(uuid)
        } else {
            studentNoteRepository.getByUuid(uuid).get()
        }
        studentNoteMapper.update(studentNoteRequest, studentNote)
        return studentNoteMapper.toDto(studentNoteRepository.save(studentNote))
    }

    @Transactional
    override fun updateMultiple(studentNoteDtoList: List<StudentNoteDto>): List<StudentNoteDto> {
        log.trace("studentNote updateMultiple -> studentNoteDtoList: ${objectMapper.writeValueAsString(studentNoteDtoList)}")
        val studentNotes = studentNoteRepository.findAllById(studentNoteDtoList.mapNotNull { it.uuid })
        studentNotes.forEach { studentNote ->
            val first = studentNoteDtoList.first { it.uuid == studentNote.uuid }
            studentNoteMapper.update(studentNoteMapper.toRequest(first), studentNote)
            if (first.note == null) {
                studentNote.note = null
            }
        }
        return studentNoteRepository.saveAll(studentNotes).map(studentNoteMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("studentNote delete -> uuid: $uuid")
        val studentNote = getById(uuid)
        studentNote.deleted = true
        studentNote.deletedAt = LocalDateTime.now()
        studentNoteRepository.save(studentNote)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("studentNote deleteMultiple -> uuid: $uuidList")
        val studentNotes = studentNoteRepository.findAllById(uuidList)
        studentNotes.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        studentNoteRepository.saveAll(studentNotes)
    }
}
