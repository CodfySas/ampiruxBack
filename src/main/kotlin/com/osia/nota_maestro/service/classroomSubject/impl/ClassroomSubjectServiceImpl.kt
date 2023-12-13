package com.osia.nota_maestro.service.classroomSubject.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectByTeacherDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectClassDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectCompleteDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectMapper
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectRequest
import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.model.Subject
import com.osia.nota_maestro.model.User
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroomSubject.ClassroomSubjectService
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

@Service("classroomSubject.crud_service")
@Transactional
class ClassroomSubjectServiceImpl(
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomSubjectMapper: ClassroomSubjectMapper,
    private val objectMapper: ObjectMapper,
    private val gradeRepository: GradeRepository,
    private val classroomRepository: ClassroomRepository,
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val subjectRepository: SubjectRepository,
    private val userRepository: UserRepository
) : ClassroomSubjectService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroomSubject count -> increment: $increment")
        return classroomSubjectRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ClassroomSubject {
        return classroomSubjectRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ClassroomSubject $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomSubjectDto> {
        log.trace("classroomSubject findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomSubjectRepository.findAllById(uuidList).map(classroomSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomSubjectDto> {
        log.trace("classroomSubject findAll -> pageable: $pageable")
        return classroomSubjectRepository.findAll(
            Specification.where(
                CreateSpec<ClassroomSubject>().createSpec(
                    "",
                    school
                )
            ),
            pageable
        ).map(classroomSubjectMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomSubjectDto> {
        log.trace("classroomSubject findAllByFilter -> pageable: $pageable, where: $where")
        return classroomSubjectRepository.findAll(
            Specification.where(
                CreateSpec<ClassroomSubject>().createSpec(
                    where,
                    school
                )
            ),
            pageable
        ).map(classroomSubjectMapper::toDto)
    }

    @Transactional
    override fun save(classroomSubjectRequest: ClassroomSubjectRequest, replace: Boolean): ClassroomSubjectDto {
        log.trace("classroomSubject save -> request: $classroomSubjectRequest")
        return classroomSubjectMapper.toDto(
            classroomSubjectRepository.save(
                classroomSubjectMapper.toModel(
                    classroomSubjectRequest
                )
            )
        )
    }

    @Transactional
    override fun saveMultiple(classroomSubjectRequestList: List<ClassroomSubjectRequest>): List<ClassroomSubjectDto> {
        log.trace(
            "classroomSubject saveMultiple -> requestList: ${
            objectMapper.writeValueAsString(
                classroomSubjectRequestList
            )
            }"
        )
        val classroomSubjects = classroomSubjectRequestList.map(classroomSubjectMapper::toModel)
        return classroomSubjectRepository.saveAll(classroomSubjects).map(classroomSubjectMapper::toDto)
    }

    @Transactional
    override fun update(
        uuid: UUID,
        classroomSubjectRequest: ClassroomSubjectRequest,
        includeDelete: Boolean
    ): ClassroomSubjectDto {
        log.trace("classroomSubject update -> uuid: $uuid, request: $classroomSubjectRequest")
        val classroomSubject = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomSubjectRepository.getByUuid(uuid).get()
        }
        classroomSubjectMapper.update(classroomSubjectRequest, classroomSubject)
        return classroomSubjectMapper.toDto(classroomSubjectRepository.save(classroomSubject))
    }

    @Transactional
    override fun updateMultiple(classroomSubjectDtoList: List<ClassroomSubjectDto>): List<ClassroomSubjectDto> {
        log.trace(
            "classroomSubject updateMultiple -> classroomSubjectDtoList: ${
            objectMapper.writeValueAsString(
                classroomSubjectDtoList
            )
            }"
        )
        val classroomSubjects = classroomSubjectRepository.findAllById(classroomSubjectDtoList.mapNotNull { it.uuid })
        classroomSubjects.forEach { classroomSubject ->
            classroomSubjectMapper.update(
                classroomSubjectMapper.toRequest(classroomSubjectDtoList.first { it.uuid == classroomSubject.uuid }),
                classroomSubject
            )
        }
        return classroomSubjectRepository.saveAll(classroomSubjects).map(classroomSubjectMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroomSubject delete -> uuid: $uuid")
        val classroomSubject = getById(uuid)
        classroomSubject.deleted = true
        classroomSubject.deletedAt = LocalDateTime.now()
        classroomSubjectRepository.save(classroomSubject)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroomSubject deleteMultiple -> uuid: $uuidList")
        val classroomSubjects = classroomSubjectRepository.findAllById(uuidList)
        classroomSubjects.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomSubjectRepository.saveAll(classroomSubjects)
    }

    override fun getCompleteInfo(school: UUID): List<ClassroomSubjectCompleteDto> {
        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school)))
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(allGrades.mapNotNull { it.uuid }, LocalDateTime.now().year)
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(allGrades.mapNotNull { it.uuid })
        val subjectsInClassrooms = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val subjects = subjectRepository.findAll(Specification.where(CreateSpec<Subject>().createSpec("", school)))
        val teachers = userRepository.findAll(Specification.where(CreateSpec<User>().createSpec("role:teacher", school)))

        val final = mutableListOf<ClassroomSubjectCompleteDto>()
        allGrades.forEach { g ->
            final.add(
                ClassroomSubjectCompleteDto().apply {
                    this.uuid = g.uuid
                    this.name = g.name ?: ""
                    this.classrooms = classrooms.filter { it.uuidGrade == g.uuid }.map { c ->
                        ClassroomSubjectClassDto().apply {
                            this.name = c.name ?: ""
                            this.uuid = c.uuid
                            this.subjects = gradeSubjects.filter { it.uuidGrade == g.uuid }.map { s ->
                                ClassroomSubjectByTeacherDto().apply {
                                    this.uuid = s.uuidSubject
                                    this.name = subjects.first { it.uuid == s.uuidSubject }.name ?: ""
                                    val subjectInClass =
                                        subjectsInClassrooms.firstOrNull { it.uuidSubject == s.uuidSubject && it.uuidClassroom == c.uuid }
                                    if (subjectInClass != null) {
                                        val teacher = teachers.firstOrNull { it.uuid == subjectInClass.uuidTeacher }
                                        this.uuidTeacher = teacher?.uuid
                                        this.teacherName = teacher?.name
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
        return final
    }
}
