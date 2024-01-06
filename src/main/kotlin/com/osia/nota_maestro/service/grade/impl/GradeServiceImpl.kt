package com.osia.nota_maestro.service.grade.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomCompleteDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomDto
import com.osia.nota_maestro.dto.classroom.v1.ClassroomMapper
import com.osia.nota_maestro.dto.classroom.v1.ClassroomRequest
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentDto
import com.osia.nota_maestro.dto.classroomStudent.v1.ClassroomStudentRequest
import com.osia.nota_maestro.dto.grade.v1.CourseInfoDto
import com.osia.nota_maestro.dto.grade.v1.GradeDto
import com.osia.nota_maestro.dto.grade.v1.GradeMapper
import com.osia.nota_maestro.dto.grade.v1.GradeRequest
import com.osia.nota_maestro.dto.grade.v1.GradeSubjectsDto
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectMapper
import com.osia.nota_maestro.dto.gradeSubject.v1.GradeSubjectRequest
import com.osia.nota_maestro.dto.user.v1.UserDto
import com.osia.nota_maestro.dto.user.v1.UserMapper
import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroom.ClassroomService
import com.osia.nota_maestro.service.classroomStudent.ClassroomStudentService
import com.osia.nota_maestro.service.grade.GradeService
import com.osia.nota_maestro.service.gradeSubject.GradeSubjectService
import com.osia.nota_maestro.service.school.SchoolService
import com.osia.nota_maestro.service.subject.SubjectService
import com.osia.nota_maestro.service.user.UserService
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

@Service("grade.crud_service")
@Transactional
class GradeServiceImpl(
    private val gradeRepository: GradeRepository,
    private val gradeMapper: GradeMapper,
    private val userMapper: UserMapper,
    private val classroomMapper: ClassroomMapper,
    private val classroomService: ClassroomService,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentService: ClassroomStudentService,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val objectMapper: ObjectMapper,
    private val gradeSubjectService: GradeSubjectService,
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val gradeSubjectMapper: GradeSubjectMapper,
    private val subjectService: SubjectService,
    private val schoolService: SchoolService
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
        val schoolFound = schoolService.getById(school)
        val grades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).map(gradeMapper::toComplete).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(grades.mapNotNull { it.uuid }, schoolFound.actualYear!!).map(classroomMapper::toComplete).sortedBy { it.name }

        val studentsInClassRooms = classroomStudentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid })
        val students = userRepository.getAllByUuidIn(studentsInClassRooms.mapNotNull { it.uuidStudent })
        val studentsWithoutClassroom = userRepository.getStudentsWithoutClassroom(school)

        classrooms.forEach {
            val myList = studentsInClassRooms.filter { s -> s.uuidClassroom == it.uuid }
            it.students = students.filter { s -> myList.mapNotNull { m -> m.uuidStudent }.contains(s.uuid) }.map(userMapper::toDto)
        }
        grades.forEach {
            it.classrooms = classrooms.filter { c -> c.uuidGrade == it.uuid }
            it.noAssignedStudents = studentsWithoutClassroom.filter { s -> s.actualGrade == it.uuid }.map(userMapper::toDto)
        }
        return CourseInfoDto().apply {
            this.grades = grades
            this.noAssignedStudents = studentsWithoutClassroom.filter { it.actualGrade == null || !grades.mapNotNull { c-> c.uuid }.contains(it.actualGrade) }.map(userMapper::toDto)
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
    override fun saveComplete(grades: CourseInfoDto, school: UUID): CourseInfoDto {
        val schoolFound = schoolService.getById(school)
        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).map(gradeMapper::toComplete)
        val gTD = allGrades.filterNot { grades.grades!!.mapNotNull { g -> g.uuid }.contains(it.uuid) }
        val cTD = mutableListOf<ClassroomCompleteDto>()
        val uTD = grades.noAssignedStudents!!.mapNotNull { it.uuid }.toMutableList()
        val classRoomReq = mutableListOf<ClassroomStudentRequest>()
        val users = mutableListOf<UUID>()
        val classRoom = mutableListOf<UUID>()
        val usersUpdate = mutableListOf<UserDto>()
        grades.grades?.forEach {
            uTD.addAll(it.noAssignedStudents!!.mapNotNull { s -> s.uuid })
            val r = GradeRequest().apply {
                this.name = it.name
                this.uuidSchool = school
                this.ordered = it.ordered
            }
            val savedGrade = if (it.uuid == null) {
                save(r)
            } else {
                val classroomsInGrade = classroomRepository.findAllByUuidGradeInAndYear(listOf(it.uuid!!), schoolFound.actualYear!!).map(classroomMapper::toComplete)
                cTD.addAll(classroomsInGrade.filterNot { cig -> it.classrooms.mapNotNull { c -> c.uuid }.contains(cig.uuid) })
                update(it.uuid!!, r)
            }
            it.classrooms.forEach { c ->
                val rc = ClassroomRequest().apply {
                    this.name = c.name
                    this.uuidSchool = school
                    this.year = schoolFound.actualYear!!
                    this.uuidGrade = savedGrade.uuid
                }
                val classroom = if (c.uuid == null) {
                    classroomService.save(rc)
                } else {
                    ClassroomDto().apply {
                        this.uuid = c.uuid
                    }
                }
                c.students?.forEach { u ->
                    usersUpdate.add(
                        UserDto().apply {
                            this.uuid = u.uuid
                            this.actualGrade = it.uuid
                        }
                    )
                    classRoomReq.add(
                        ClassroomStudentRequest().apply {
                            this.uuidClassroom = classroom.uuid
                            this.uuidStudent = u.uuid
                        }
                    )
                }
                classRoom.add(classroom.uuid!!)
                users.addAll(c.students!!.mapNotNull { s -> s.uuid })
            }
        }
        gradeRepository.deleteByUuids(gTD.mapNotNull { it.uuid })
        classroomRepository.deleteByUuids(cTD.mapNotNull { it.uuid })

        val actives = classroomStudentRepository.findAllByUuidStudentInAndUuidClassroomIn(users, classRoom)

        val csTU = mutableListOf<ClassroomStudentDto>()
        val csTS = mutableListOf<ClassroomStudentRequest>()
        classRoomReq.forEach {
            if (actives.mapNotNull { a -> a.uuidStudent }.contains(it.uuidStudent)) {
                val actualSaved = actives.first { a -> a.uuidStudent == it.uuidStudent }
                if (!uTD.contains(it.uuidStudent)) {
                    csTU.add(
                        ClassroomStudentDto().apply {
                            this.uuidClassroom = it.uuidClassroom
                            this.uuid = actualSaved.uuid
                        }
                    )
                }
            } else {
                csTS.add(
                    ClassroomStudentRequest().apply {
                        this.uuidClassroom = it.uuidClassroom
                        this.uuidStudent = it.uuidStudent
                    }
                )
            }
        }
        val csTd = actives.mapNotNull { a -> a.uuid }.filterNot { a -> csTU.mapNotNull { it.uuid }.contains(a) }
        classroomStudentRepository.deleteByUuids(csTd)
        classroomStudentRepository.deleteByUuidStudentsAndClassRooms(uTD, classRoom)

        classroomStudentService.saveMultiple(csTS)
        classroomStudentService.updateMultiple(csTU)
        userService.updateMultiple(usersUpdate)
        return grades
    }

    @Transactional
    override fun saveGradeSubjects(gradeSubjectsDto: List<GradeSubjectsDto>, school: UUID): List<GradeSubjectsDto> {
        val gradeSubjectsByGrade = gradeSubjectRepository.findAllByUuidGradeIn(gradeSubjectsDto.mapNotNull { it.uuid })
        gradeSubjectsDto.forEach {
            val subjects = it.subjects
            val savedGrade = gradeSubjectsByGrade.filter { g -> g.uuidGrade == it.uuid }
            val toDelete = savedGrade.filterNot { sg -> subjects.mapNotNull { s -> s.uuid }.contains(sg.uuidSubject) }
            gradeSubjectRepository.deleteByUuids(toDelete.mapNotNull { t -> t.uuid })
            it.subjects.forEach { sub ->
                if (!savedGrade.mapNotNull { sx-> sx.uuidSubject }.contains(sub.uuid)) {
                    gradeSubjectService.save(
                        GradeSubjectRequest().apply {
                            this.uuidSubject = sub.uuid
                            this.uuidGrade = it.uuid
                        }
                    )
                }
            }
        }
        return gradeSubjectsDto
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
        gradeRepository.delete(grade)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("grade deleteMultiple -> uuid: $uuidList")
        gradeRepository.deleteAllById(uuidList)
    }

    override fun getGradeWithSubjects(school: UUID): List<GradeSubjectsDto> {
        val allGrades = gradeRepository.findAll(Specification.where(CreateSpec<Grade>().createSpec("", school))).map(gradeMapper::toComplete)
        val gradeSubject = gradeSubjectRepository.findAllByUuidGradeIn(allGrades.mapNotNull { it.uuid })
        val final = mutableListOf<GradeSubjectsDto>()
        val subjects = subjectService.findByMultiple(gradeSubject.mapNotNull { it.uuidSubject })
        allGrades.forEach {
            val sg = gradeSubject.filter { g -> g.uuidGrade == it.uuid }.map(gradeSubjectMapper::toDto)
            final.add(
                GradeSubjectsDto().apply {
                    this.uuid = it.uuid
                    this.name = it.name
                    this.subjects = subjects.filter { s -> sg.mapNotNull { g -> g.uuidSubject }.contains(s.uuid) }
                }
            )
        }
        return final
    }
}
