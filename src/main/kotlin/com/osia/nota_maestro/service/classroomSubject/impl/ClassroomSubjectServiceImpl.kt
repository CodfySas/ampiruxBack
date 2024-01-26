package com.osia.nota_maestro.service.classroomSubject.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectByTeacherDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectClassDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectCompleteDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectDto
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectMapper
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomSubjectRequest
import com.osia.nota_maestro.dto.classroomSubject.v1.ClassroomsTeachersDto
import com.osia.nota_maestro.dto.classroomSubject.v1.CompleteSubjectsTeachersDto
import com.osia.nota_maestro.dto.classroomSubject.v1.GradeTeachersDto
import com.osia.nota_maestro.dto.classroomSubject.v1.SubjectTeachersDto
import com.osia.nota_maestro.dto.subject.v1.SubjectMapper
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.model.ClassroomSubject
import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.model.Subject
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroomSubject.ClassroomSubjectService
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
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val subjectMapper: SubjectMapper,
    private val schoolRepository: SchoolService
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
        val schoolFound = schoolRepository.getById(school)
        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school)))
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(allGrades.mapNotNull { it.uuid }, schoolFound.actualYear!!)
        val gradeSubjects = gradeSubjectRepository.findAllByUuidGradeIn(allGrades.mapNotNull { it.uuid })
        val subjectsInClassrooms = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val subjects = subjectRepository.findAll(Specification.where(CreateSpec<Subject>().createSpec("", school))).filter { it.isParent != true }
        val teachers = userRepository.findAllByRoleAndUuidSchool("teacher", school).sortedBy { it.name }

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
                            this.subjects = gradeSubjects.filter { it.uuidGrade == g.uuid && subjects.mapNotNull { sd -> sd.uuid }.contains(it.uuidSubject) }.map { s ->
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

    override fun getCompleteInfo2(school: UUID): CompleteSubjectsTeachersDto {
        val schoolFound = schoolRepository.getById(school)
        val subjects = subjectRepository.findAll(Specification.where(CreateSpec<Subject>().createSpec("", school))).sortedByDescending { it.code }.filter { it.isParent != true }
        val teachers = userRepository.findAllByRoleAndUuidSchool("teacher", school).sortedBy { it.name }
        val gradeSubjects = gradeSubjectRepository.findAllByUuidSubjectIn(subjects.mapNotNull { it.uuid })

        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(allGrades.mapNotNull { it.uuid }, schoolFound.actualYear!!).sortedBy { it.name }
        val subjectsInClassrooms = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })

        val subjectList = mutableListOf<SubjectTeachersDto>()
        subjects.forEach { s ->
            subjectList.add(
                SubjectTeachersDto().apply {
                    this.uuid = s.uuid
                    this.name = s.name
                    this.grades = gradeSubjects.filter { it.uuidSubject == s.uuid }.map { g ->
                        val grade = allGrades.firstOrNull { a -> a.uuid == g.uuidGrade }
                        GradeTeachersDto().apply {
                            this.uuid = grade?.uuid
                            this.name = grade?.name
                            this.classrooms = classrooms.filter { it.uuidGrade == grade?.uuid }.map { c ->
                                ClassroomsTeachersDto().apply {
                                    val savedTeacher = subjectsInClassrooms.firstOrNull { si -> si.uuidSubject == s.uuid && si.uuidClassroom == c.uuid }
                                    this.uuid = c.uuid
                                    this.name = c.name
                                    this.uuidTeacher = savedTeacher?.uuidTeacher
                                    this.nameTeacher = savedTeacher?.let { teachers.firstOrNull { t -> t.uuid == it.uuid } }?.name
                                }
                            }
                        }
                    }
                }
            )
        }

        return CompleteSubjectsTeachersDto().apply {
            this.teachers = teachers.map(userMapper::toDto)
            this.subjects = subjectList
        }
    }

    override fun saveCompleteInfo(toSave: CompleteSubjectsTeachersDto, school: UUID): CompleteSubjectsTeachersDto {
        val schoolFound = schoolRepository.getById(school)
        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).sortedBy { it.ordered }

        val toUpdate = mutableMapOf<UUID, ClassroomSubjectRequest>()
        val toSaveR = mutableListOf<ClassroomSubjectRequest>()
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(allGrades.mapNotNull { it.uuid }, schoolFound.actualYear!!).sortedBy { it.name }
        val subjectsInClassrooms = classroomSubjectRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })

        val inded = mutableListOf<Pair<UUID, UUID>>()

        toSave.subjects?.forEach { s ->
            s.grades?.forEach { g ->
                g.classrooms?.forEach { c ->
                    inded.add(Pair(s.uuid!!, c.uuid!!))
                    val new = ClassroomSubjectRequest().apply {
                        this.uuidSubject = s.uuid
                        this.uuidTeacher = c.uuidTeacher
                        this.uuidClassroom = c.uuid
                    }
                    val found = subjectsInClassrooms.firstOrNull { it.uuidSubject == s.uuid && it.uuidClassroom == c.uuid }
                    if (found != null) {
                        val foundUuid = found.uuid!!
                        toUpdate[foundUuid] = new
                    } else {
                        toSaveR.add(new)
                    }
                }
            }
        }
        val toDelete = subjectsInClassrooms.filterNot { inded.contains(Pair(it.uuidSubject, it.uuidClassroom)) }

        saveMultiple(toSaveR)
        updateMultiple(
            toUpdate.map {
                ClassroomSubjectDto().apply {
                    this.uuid = it.key
                    this.uuidTeacher = it.value.uuidTeacher
                }
            }
        )
        classroomSubjectRepository.deleteByUuids(toDelete.mapNotNull { it.uuid })

        return toSave
    }
}
