package com.osia.nota_maestro.service.student.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.student.v1.StudentDto
import com.osia.nota_maestro.dto.student.v1.StudentMapper
import com.osia.nota_maestro.dto.student.v1.StudentRequest
import com.osia.nota_maestro.model.Student
import com.osia.nota_maestro.repository.student.StudentRepository
import com.osia.nota_maestro.service.student.StudentService
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

@Service("student.crud_service")
@Transactional
class StudentServiceImpl(
    private val studentRepository: StudentRepository,
    private val studentMapper: StudentMapper,
    private val objectMapper: ObjectMapper
) : StudentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("student count -> increment: $increment")
        return studentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Student {
        return studentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Student $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<StudentDto> {
        log.trace("student findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return studentRepository.findAllById(uuidList).map(studentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<StudentDto> {
        log.trace("student findAll -> pageable: $pageable")
        return studentRepository.findAll(Specification.where(CreateSpec<Student>().createSpec("", school)), pageable).map(studentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<StudentDto> {
        log.trace("student findAllByFilter -> pageable: $pageable, where: $where")
        return studentRepository.findAll(Specification.where(CreateSpec<Student>().createSpec(where, school)), pageable).map(studentMapper::toDto)
    }

    @Transactional
    override fun save(studentRequest: StudentRequest, replace: Boolean): StudentDto {
        log.trace("student save -> request: $studentRequest")
        val savedStudent = studentRepository.findFirstByDni(studentRequest.dni!!)
        val student = if(savedStudent.isPresent){
            if(!replace){
                throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ya existe el estudiante ${studentRequest.dni}")
            }else{
                studentMapper.update(studentRequest, savedStudent.get())
                savedStudent.get()
            }
        }else{
            studentMapper.toModel(studentRequest)
        }
        return studentMapper.toDto(studentRepository.save(student))
    }

    @Transactional
    override fun saveMultiple(studentRequestList: List<StudentRequest>): List<StudentDto> {
        log.trace("student saveMultiple -> requestList: ${objectMapper.writeValueAsString(studentRequestList)}")
        val students = studentRequestList.map(studentMapper::toModel)
        return studentRepository.saveAll(students).map(studentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, studentRequest: StudentRequest): StudentDto {
        log.trace("student update -> uuid: $uuid, request: $studentRequest")
        val student = getById(uuid)
        studentMapper.update(studentRequest, student)
        return studentMapper.toDto(studentRepository.save(student))
    }

    @Transactional
    override fun updateMultiple(studentDtoList: List<StudentDto>): List<StudentDto> {
        log.trace("student updateMultiple -> studentDtoList: ${objectMapper.writeValueAsString(studentDtoList)}")
        val students = studentRepository.findAllById(studentDtoList.mapNotNull { it.uuid })
        students.forEach { student ->
            studentMapper.update(studentMapper.toRequest(studentDtoList.first { it.uuid == student.uuid }), student)
        }
        return studentRepository.saveAll(students).map(studentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("student delete -> uuid: $uuid")
        val student = getById(uuid)
        student.deleted = true
        student.deletedAt = LocalDateTime.now()
        studentRepository.save(student)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("student deleteMultiple -> uuid: $uuidList")
        val students = studentRepository.findAllById(uuidList)
        students.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        studentRepository.saveAll(students)
    }
}
