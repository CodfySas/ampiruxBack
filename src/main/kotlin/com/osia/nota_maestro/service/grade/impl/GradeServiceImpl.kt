package com.osia.nota_maestro.service.grade.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomMapper
import com.osia.nota_maestro.dto.grade.v1.CourseInfoDto
import com.osia.nota_maestro.dto.grade.v1.GradeCompleteDto
import com.osia.nota_maestro.dto.grade.v1.GradeDto
import com.osia.nota_maestro.dto.grade.v1.GradeMapper
import com.osia.nota_maestro.dto.grade.v1.GradeRequest
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.grade.GradeService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Service("grade.crud_service")
@Transactional
class GradeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val gradeMapper: GradeMapper,
    private val userMapper: UserMapper,
    private val classroomMapper: ClassroomMapper,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper
) : GradeService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("grade count -> increment: $increment")
        return gradeRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Grade {
        return gradeRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Grade $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<GradeDto> {
        log.trace("grade findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return gradeRepository.findAllById(uuidList).map(gradeMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<GradeDto> {
        log.trace("grade findAll -> pageable: $pageable")
        return gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school)), pageable).map(gradeMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findCompleteInfo(school: UUID): CourseInfoDto {
        log.trace("grade findCompleteInfo -> school: $school")
        val grades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).map(gradeMapper::toComplete)
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(grades.mapNotNull { it.uuid }, LocalDateTime.now().year).map(classroomMapper::toComplete)

        val studentsInClassRooms = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val students = userRepository.getAllByUuidIn(studentsInClassRooms.mapNotNull { it.uuidStudent })
        val studentsWithoutClassroom = userRepository.getStudentsWithoutClassroom()

        classrooms.forEach {
            val myList = studentsInClassRooms.filter { s-> s.uuidClassroom == it.uuid }
            it.students = students.filter { s-> myList.mapNotNull { m-> m.uuidStudent }.contains(s.uuid) }.map(userMapper::toDto)
        }
        grades.forEach {
            it.classrooms = classrooms.filter { c-> c.uuidGrade == it.uuid }
            it.noAssignedStudents = studentsWithoutClassroom.filter { s-> s.actualGrade == it.uuid }.map(userMapper::toDto)
        }
        return CourseInfoDto().apply {
            this.grades = grades
            this.noAssignedStudents = studentsWithoutClassroom.filter { it.actualGrade == null || it.actualGrade == UUID.fromString("00000000-0000-0000-0000-000000000000") }.map(userMapper::toDto)
        }
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<GradeDto> {
        log.trace("grade findAllByFilter -> pageable: $pageable, where: $where")
        return gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec(where, school)), pageable).map(gradeMapper::toDto)
    }

    @Transactional
    override fun save(gradeRequest: GradeRequest, replace: Boolean): GradeDto {
        log.trace("grade save -> request: $gradeRequest")
        val savedGrade = gradeMapper.toModel(gradeRequest)
        return gradeMapper.toDto(gradeRepository.save(savedGrade))
    }

    @Transactional
    override fun saveMultiple(gradeRequestList: List<GradeRequest>): List<GradeDto> {
        log.trace("grade saveMultiple -> requestList: ${objectMapper.writeValueAsString(gradeRequestList)}")
        val grades = gradeRequestList.map(gradeMapper::toModel)
        return gradeRepository.saveAll(grades).map(gradeMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, gradeRequest: GradeRequest, includeDelete: Boolean): GradeDto {
        log.trace("grade update -> uuid: $uuid, request: $gradeRequest")
        val grade = if (!includeDelete) {
            getById(uuid)
        } else {
            gradeRepository.getByUuid(uuid).get()
        }
        gradeMapper.update(gradeRequest, grade)
        return gradeMapper.toDto(gradeRepository.save(grade))
    }

    @Transactional
    override fun updateMultiple(gradeDtoList: List<GradeDto>): List<GradeDto> {
        log.trace("grade updateMultiple -> gradeDtoList: ${objectMapper.writeValueAsString(gradeDtoList)}")
        val grades = gradeRepository.findAllById(gradeDtoList.mapNotNull { it.uuid })
        grades.forEach { grade ->
            gradeMapper.update(gradeMapper.toRequest(gradeDtoList.first { it.uuid == grade.uuid }), grade)
        }
        return gradeRepository.saveAll(grades).map(gradeMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("grade delete -> uuid: $uuid")
        val grade = getById(uuid)
        grade.deleted = true
        grade.deletedAt = LocalDateTime.now()
        gradeRepository.save(grade)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("grade deleteMultiple -> uuid: $uuidList")
        val grades = gradeRepository.findAllById(uuidList)
        grades.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        gradeRepository.saveAll(grades)
    }
}
