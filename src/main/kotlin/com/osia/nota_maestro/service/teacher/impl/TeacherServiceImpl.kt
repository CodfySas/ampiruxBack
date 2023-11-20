package com.osia.nota_maestro.service.teacher.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.teacher.v1.TeacherDto
import com.osia.nota_maestro.dto.teacher.v1.TeacherMapper
import com.osia.nota_maestro.dto.teacher.v1.TeacherRequest
import com.osia.nota_maestro.model.Teacher
import com.osia.nota_maestro.repository.teacher.TeacherRepository
import com.osia.nota_maestro.service.teacher.TeacherService
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

@Service("teacher.crud_service")
@Transactional
class TeacherServiceImpl(
    private val teacherRepository: TeacherRepository,
    private val teacherMapper: TeacherMapper,
    private val objectMapper: ObjectMapper
) : TeacherService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("teacher count -> increment: $increment")
        return teacherRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Teacher {
        return teacherRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Teacher $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<TeacherDto> {
        log.trace("teacher findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return teacherRepository.findAllById(uuidList).map(teacherMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<TeacherDto> {
        log.trace("teacher findAll -> pageable: $pageable")
        return teacherRepository.findAll(Specification.where(CreateSpec<Teacher>().createSpec("", school)), pageable).map(teacherMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<TeacherDto> {
        log.trace("teacher findAllByFilter -> pageable: $pageable, where: $where")
        return teacherRepository.findAll(Specification.where(CreateSpec<Teacher>().createSpec(where, school)), pageable).map(teacherMapper::toDto)
    }

    @Transactional
    override fun save(teacherRequest: TeacherRequest, replace: Boolean): TeacherDto {
        log.trace("teacher save -> request: $teacherRequest")
        val savedTeacher = teacherRepository.findFirstByDni(teacherRequest.dni!!)
        val teacher = if(savedTeacher.isPresent){
            if(!replace){
                throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Ya existe el docente ${teacherRequest.dni}")
            }else{
                teacherMapper.update(teacherRequest, savedTeacher.get())
                savedTeacher.get()
            }
        }else{
            teacherMapper.toModel(teacherRequest)
        }
        return teacherMapper.toDto(teacherRepository.save(teacher))
    }

    @Transactional
    override fun saveMultiple(teacherRequestList: List<TeacherRequest>): List<TeacherDto> {
        log.trace("teacher saveMultiple -> requestList: ${objectMapper.writeValueAsString(teacherRequestList)}")
        val teachers = teacherRequestList.map(teacherMapper::toModel)
        return teacherRepository.saveAll(teachers).map(teacherMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, teacherRequest: TeacherRequest): TeacherDto {
        log.trace("teacher update -> uuid: $uuid, request: $teacherRequest")
        val teacher = getById(uuid)
        teacherMapper.update(teacherRequest, teacher)
        return teacherMapper.toDto(teacherRepository.save(teacher))
    }

    @Transactional
    override fun updateMultiple(teacherDtoList: List<TeacherDto>): List<TeacherDto> {
        log.trace("teacher updateMultiple -> teacherDtoList: ${objectMapper.writeValueAsString(teacherDtoList)}")
        val teachers = teacherRepository.findAllById(teacherDtoList.mapNotNull { it.uuid })
        teachers.forEach { teacher ->
            teacherMapper.update(teacherMapper.toRequest(teacherDtoList.first { it.uuid == teacher.uuid }), teacher)
        }
        return teacherRepository.saveAll(teachers).map(teacherMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("teacher delete -> uuid: $uuid")
        val teacher = getById(uuid)
        teacher.deleted = true
        teacher.deletedAt = LocalDateTime.now()
        teacherRepository.save(teacher)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("teacher deleteMultiple -> uuid: $uuidList")
        val teachers = teacherRepository.findAllById(uuidList)
        teachers.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        teacherRepository.saveAll(teachers)
    }
}
