package com.osia.nota_maestro.service.accompaniment.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentCompleteDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentMapper
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentRequest
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentRequest
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentSubjectDto
import com.osia.nota_maestro.model.Accompaniment
import com.osia.nota_maestro.repository.accompaniment.AccompanimentRepository
import com.osia.nota_maestro.repository.accompanimentStudent.AccompanimentStudentRepository
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.accompaniment.AccompanimentService
import com.osia.nota_maestro.service.accompanimentStudent.AccompanimentStudentService
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

@Service("accompaniment.crud_service")
@Transactional
class AccompanimentServiceImpl(
    private val accompanimentRepository: AccompanimentRepository,
    private val accompanimentMapper: AccompanimentMapper,
    private val objectMapper: ObjectMapper,
    private val classroomRepository: ClassroomRepository,
    private val gradeRepository: GradeRepository,
    private val schoolService: SchoolService,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val accompanimentStudentRepository: AccompanimentStudentRepository,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val subjectRepository: SubjectRepository,
    private val userRepository: UserRepository,
    private val accompanimentStudentService: AccompanimentStudentService
) : AccompanimentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("accompaniment count -> increment: $increment")
        return accompanimentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Accompaniment {
        return accompanimentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Accompaniment $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<AccompanimentDto> {
        log.trace("accompaniment findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return accompanimentRepository.findAllById(uuidList).map(accompanimentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<AccompanimentDto> {
        log.trace("accompaniment findAll -> pageable: $pageable")
        return accompanimentRepository.findAll(
            Specification.where(CreateSpec<Accompaniment>().createSpec("", school)),
            pageable
        ).map(accompanimentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AccompanimentDto> {
        log.trace("accompaniment findAllByFilter -> pageable: $pageable, where: $where")
        return accompanimentRepository.findAll(
            Specification.where(
                CreateSpec<Accompaniment>().createSpec(
                    where,
                    school
                )
            ),
            pageable
        ).map(accompanimentMapper::toDto)
    }

    @Transactional
    override fun save(accompanimentRequest: AccompanimentRequest, replace: Boolean): AccompanimentDto {
        log.trace("accompaniment save -> request: $accompanimentRequest")
        val savedAccompaniment = accompanimentMapper.toModel(accompanimentRequest)
        return accompanimentMapper.toDto(accompanimentRepository.save(savedAccompaniment))
    }

    @Transactional
    override fun saveMultiple(accompanimentRequestList: List<AccompanimentRequest>): List<AccompanimentDto> {
        log.trace("accompaniment saveMultiple -> requestList: ${objectMapper.writeValueAsString(accompanimentRequestList)}")
        val accompaniments = accompanimentRequestList.map(accompanimentMapper::toModel)
        return accompanimentRepository.saveAll(accompaniments).map(accompanimentMapper::toDto)
    }

    @Transactional
    override fun update(
        uuid: UUID,
        accompanimentRequest: AccompanimentRequest,
        includeDelete: Boolean
    ): AccompanimentDto {
        log.trace("accompaniment update -> uuid: $uuid, request: $accompanimentRequest")
        val accompaniment = if (!includeDelete) {
            getById(uuid)
        } else {
            accompanimentRepository.getByUuid(uuid).get()
        }
        accompanimentMapper.update(accompanimentRequest, accompaniment)
        return accompanimentMapper.toDto(accompanimentRepository.save(accompaniment))
    }

    @Transactional
    override fun updateMultiple(accompanimentDtoList: List<AccompanimentDto>): List<AccompanimentDto> {
        log.trace(
            "accompaniment updateMultiple -> accompanimentDtoList: ${
            objectMapper.writeValueAsString(
                accompanimentDtoList
            )
            }"
        )
        val accompaniments = accompanimentRepository.findAllById(accompanimentDtoList.mapNotNull { it.uuid })
        accompaniments.forEach { accompaniment ->
            accompanimentMapper.update(
                accompanimentMapper.toRequest(accompanimentDtoList.first { it.uuid == accompaniment.uuid }),
                accompaniment
            )
        }
        return accompanimentRepository.saveAll(accompaniments).map(accompanimentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("accompaniment delete -> uuid: $uuid")
        val accompaniment = getById(uuid)
        accompaniment.deleted = true
        accompaniment.deletedAt = LocalDateTime.now()
        accompanimentRepository.save(accompaniment)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("accompaniment deleteMultiple -> uuid: $uuidList")
        val accompaniments = accompanimentRepository.findAllById(uuidList)
        accompaniments.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        accompanimentRepository.saveAll(accompaniments)
    }

    override fun getComplete(school: UUID): List<AccompanimentCompleteDto> {
        val mySchool = schoolService.getById(school)
        val grades = gradeRepository.findAllByUuidSchool(school).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(
            grades.mapNotNull { it.uuid }.distinct(),
            mySchool.actualYear!!
        )
        val accompaniments =
            accompanimentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid }.distinct())
        val final = mutableListOf<AccompanimentCompleteDto>()
        grades.forEach {
            val myClassrooms = classrooms.filter { c -> c.uuidGrade == it.uuid }
            myClassrooms.forEach { c ->
                final.add(
                    AccompanimentCompleteDto().apply {
                        val accompanimentFounds = accompaniments.filter { d -> d.uuidClassroom == c.uuid }
                        this.classroomName = c.name
                        this.gradeName = it.name
                        this.uuidClassroom = c.uuid
                        this.accompaniments = accompanimentFounds.map(accompanimentMapper::toDto)
                    }
                )
            }
        }
        return final
    }

    override fun saveComplete(complete: List<AccompanimentCompleteDto>, school: UUID): List<AccompanimentCompleteDto> {
        val mySchool = schoolService.getById(school)
        val grades = gradeRepository.findAllByUuidSchool(school).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(
            grades.mapNotNull { it.uuid }.distinct(),
            mySchool.actualYear!!
        )
        val accompaniments =
            accompanimentRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid }.distinct())
        val toUpdate = mutableListOf<AccompanimentDto>()
        val toCreate = mutableListOf<AccompanimentRequest>()
        val allToSave = complete.flatMap { it.accompaniments ?: mutableListOf() }.mapNotNull { it.uuid }
        val toDelete = accompaniments.filterNot { allToSave.contains(it.uuid) }.mapNotNull { it.uuid }
        complete.forEach {
            it.accompaniments?.forEach { a ->
                if (a.uuid != null) {
                    toUpdate.add(
                        AccompanimentDto().apply {
                            this.uuid = a.uuid
                            this.uuidTeacher = a.uuidTeacher
                            this.uuidClassroom = a.uuidClassroom
                        }
                    )
                } else {
                    toCreate.add(
                        AccompanimentRequest().apply {
                            this.uuidTeacher = a.uuidTeacher
                            this.uuidClassroom = a.uuidClassroom
                        }
                    )
                }
            }
        }
        saveMultiple(toCreate)
        updateMultiple(toUpdate)
        deleteMultiple(toDelete)
        return complete
    }

    override fun getMyGroups(uuid: UUID, school: UUID): List<AccompanimentCompleteDto> {
        val schoolFound = schoolService.getById(school)
        val accompanimentsByTeacher = accompanimentRepository.getAllByUuidTeacher(uuid)
        val classrooms = classroomRepository.findAllById(accompanimentsByTeacher.mapNotNull { it.uuidClassroom })
            .filter { c -> c.year == schoolFound.actualYear!! }
        val accompanimentsYear =
            accompanimentsByTeacher.filter { d -> classrooms.mapNotNull { it.uuid }.contains(d.uuidClassroom) }
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })

        val final = mutableListOf<AccompanimentCompleteDto>()
        accompanimentsYear.forEach {
            final.add(
                AccompanimentCompleteDto().apply {
                    val classroomFound = classrooms.firstOrNull { c -> c.uuid == it.uuidClassroom }
                    this.uuid = it.uuid
                    this.uuidClassroom = classroomFound?.uuid
                    this.classroomName = classroomFound?.name
                    this.gradeName = grades.firstOrNull { g -> g.uuid == classroomFound?.uuidGrade }?.name
                }
            )
        }
        return final
    }

    override fun getByClassroom(uuid: UUID, period: Int): List<AccompanimentStudentDto> {
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(uuid)
        val accompanimentStudents = accompanimentStudentRepository.getAllByUuidClassroomStudentInAndPeriod(
            classroomStudents.mapNotNull { it.uuid },
            period
        )
        val studentSubjectsInPeriod =
            studentSubjectRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid })
                .filter { it.period == period }
        val users = userRepository.findAllById(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val subjects = subjectRepository.findAllById(studentSubjectsInPeriod.mapNotNull { it.uuidSubject }.distinct())

        return classroomStudents.map { cs ->
            val myAccompanimentStudent = accompanimentStudents.firstOrNull { d -> d.uuidClassroomStudent == cs.uuid }
            val myUser = users.firstOrNull { u -> u.uuid == cs.uuidStudent }
            AccompanimentStudentDto().apply {
                this.description = myAccompanimentStudent?.description
                this.uuid = myAccompanimentStudent?.uuid
                this.name = myUser?.name
                this.lastname = myUser?.lastname
                this.period = period
                this.uuidClassroomStudent = cs.uuid
                this.uuidStudent = cs.uuidStudent
                this.studentSubjects =
                    studentSubjectsInPeriod.filter { ss -> ss.uuidClassroomStudent == cs.uuid }.map { ss ->
                        AccompanimentStudentSubjectDto().apply {
                            val subjectFound = subjects.firstOrNull { s -> s.uuid == ss.uuidSubject }
                            this.name = subjectFound?.name
                            this.uuid = subjectFound?.uuid
                            this.def = ss.def
                            this.recovery = ss.recovery
                            this.judgment = ss.judgment
                            this.observation = ss.observation
                        }
                    }
            }
        }
    }

    override fun submit(uuid: UUID, period: Int, req: List<AccompanimentStudentDto>): List<AccompanimentStudentDto> {
        val toSave = mutableListOf<AccompanimentStudentRequest>()
        val toUpdate = mutableListOf<AccompanimentStudentDto>()
        req.forEach {
            if (it.uuid == null) {
                toSave.add(
                    AccompanimentStudentRequest().apply {
                        this.uuidClassroomStudent = it.uuidClassroomStudent
                        this.description = it.description ?: ""
                        this.period = period
                        this.uuidStudent = it.uuidStudent
                    }
                )
            } else {
                toUpdate.add(
                    AccompanimentStudentDto().apply {
                        this.uuidClassroomStudent = it.uuidClassroomStudent
                        this.description = it.description
                        this.period = period
                        this.uuidStudent = it.uuidStudent
                        this.uuid = it.uuid
                    }
                )
            }
        }
        if (toSave.isNotEmpty()) {
            accompanimentStudentService.saveMultiple(toSave)
        }
        if (toUpdate.isNotEmpty()) {
            accompanimentStudentService.updateMultiple(toUpdate)
        }
        return req
    }
}
