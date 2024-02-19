package com.osia.nota_maestro.service.director.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.director.v1.DirectorCompleteDto
import com.osia.nota_maestro.dto.director.v1.DirectorDto
import com.osia.nota_maestro.dto.director.v1.DirectorMapper
import com.osia.nota_maestro.dto.director.v1.DirectorRequest
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentSubjectDto
import com.osia.nota_maestro.model.Director
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.director.DirectorRepository
import com.osia.nota_maestro.repository.directorStudent.DirectorStudentRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.studentSubject.StudentSubjectRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.director.DirectorService
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

@Service("director.crud_service")
@Transactional
class DirectorServiceImpl(
    private val directorRepository: DirectorRepository,
    private val directorMapper: DirectorMapper,
    private val objectMapper: ObjectMapper,
    private val classroomRepository: ClassroomRepository,
    private val gradeRepository: GradeRepository,
    private val schoolService: SchoolService,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val directorStudentRepository: DirectorStudentRepository,
    private val studentSubjectRepository: StudentSubjectRepository,
    private val subjectRepository: SubjectRepository,
    private val userRepository: UserRepository,
) : DirectorService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("director count -> increment: $increment")
        return directorRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Director {
        return directorRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Director $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<DirectorDto> {
        log.trace("director findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return directorRepository.findAllById(uuidList).map(directorMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<DirectorDto> {
        log.trace("director findAll -> pageable: $pageable")
        return directorRepository.findAll(Specification.where(CreateSpec<Director>().createSpec("", school)), pageable).map(directorMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<DirectorDto> {
        log.trace("director findAllByFilter -> pageable: $pageable, where: $where")
        return directorRepository.findAll(Specification.where(CreateSpec<Director>().createSpec(where, school)), pageable).map(directorMapper::toDto)
    }

    @Transactional
    override fun save(directorRequest: DirectorRequest, replace: Boolean): DirectorDto {
        log.trace("director save -> request: $directorRequest")
        val savedDirector = directorMapper.toModel(directorRequest)
        return directorMapper.toDto(directorRepository.save(savedDirector))
    }

    @Transactional
    override fun saveMultiple(directorRequestList: List<DirectorRequest>): List<DirectorDto> {
        log.trace("director saveMultiple -> requestList: ${objectMapper.writeValueAsString(directorRequestList)}")
        val directors = directorRequestList.map(directorMapper::toModel)
        return directorRepository.saveAll(directors).map(directorMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, directorRequest: DirectorRequest, includeDelete: Boolean): DirectorDto {
        log.trace("director update -> uuid: $uuid, request: $directorRequest")
        val director = if (!includeDelete) {
            getById(uuid)
        } else {
            directorRepository.getByUuid(uuid).get()
        }
        directorMapper.update(directorRequest, director)
        return directorMapper.toDto(directorRepository.save(director))
    }

    @Transactional
    override fun updateMultiple(directorDtoList: List<DirectorDto>): List<DirectorDto> {
        log.trace("director updateMultiple -> directorDtoList: ${objectMapper.writeValueAsString(directorDtoList)}")
        val directors = directorRepository.findAllById(directorDtoList.mapNotNull { it.uuid })
        directors.forEach { director ->
            directorMapper.update(directorMapper.toRequest(directorDtoList.first { it.uuid == director.uuid }), director)
        }
        return directorRepository.saveAll(directors).map(directorMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("director delete -> uuid: $uuid")
        val director = getById(uuid)
        director.deleted = true
        director.deletedAt = LocalDateTime.now()
        directorRepository.save(director)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("director deleteMultiple -> uuid: $uuidList")
        val directors = directorRepository.findAllById(uuidList)
        directors.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        directorRepository.saveAll(directors)
    }

    override fun getComplete(school: UUID): List<DirectorCompleteDto> {
        val mySchool = schoolService.getById(school)
        val grades = gradeRepository.findAllByUuidSchool(school).sortedBy { it.ordered }
        val classrooms = classroomRepository.findAllByUuidGradeInAndYear(grades.mapNotNull { it.uuid }.distinct(), mySchool.actualYear!!)
        val directors = directorRepository.getAllByUuidClassroomIn(classrooms.mapNotNull { it.uuid }.distinct())
        val final = mutableListOf<DirectorCompleteDto>()
        grades.forEach {
            val myClassrooms = classrooms.filter { c-> c.uuidGrade == it.uuid }
            myClassrooms.forEach { c->
                final.add(DirectorCompleteDto().apply {
                    val directorFound = directors.firstOrNull { d-> d.uuidClassroom == c.uuid }
                    this.classroomName = c.name
                    this.gradeName = it.name
                    this.uuidClassroom = c.uuid
                    this.uuidTeacher = directorFound?.uuidTeacher
                    this.uuid = directorFound?.uuid
                })
            }
        }
        return final
    }

    override fun saveComplete(complete: List<DirectorCompleteDto>): List<DirectorCompleteDto> {
        val toUpdate = mutableListOf<DirectorDto>()
        val toCreate = mutableListOf<DirectorRequest>()
        val toDelete = mutableListOf<UUID>()
        complete.forEach {
            if(it.uuid != null){
                if(it.uuidTeacher == null){
                    toDelete.add(it.uuid!!)
                }else {
                    toUpdate.add(DirectorDto().apply {
                        this.uuid = it.uuid
                        this.uuidTeacher = it.uuidTeacher
                        this.uuidClassroom = it.uuidClassroom
                    })
                }
            }else{
                toCreate.add(DirectorRequest().apply {
                    this.uuidTeacher = it.uuidTeacher
                    this.uuidClassroom = it.uuidClassroom
                })
            }
        }
        saveMultiple(toCreate)
        updateMultiple(toUpdate)
        deleteMultiple(toDelete)

        return complete
    }

    override fun getMyGroups(uuid: UUID, school: UUID): List<DirectorCompleteDto> {
        val schoolFound = schoolService.getById(school)
        val directorsByTeacher = directorRepository.getAllByUuidTeacher(uuid)
        val classrooms = classroomRepository.findAllById(directorsByTeacher.mapNotNull { it.uuidClassroom }).filter { c-> c.year == schoolFound.actualYear!! }
        val directorsYear = directorsByTeacher.filter { d-> classrooms.mapNotNull { it.uuid }.contains(d.uuidClassroom) }
        val grades = gradeRepository.findAllById(classrooms.mapNotNull { it.uuidGrade })

        val final = mutableListOf<DirectorCompleteDto>()
        directorsYear.forEach {
            final.add(DirectorCompleteDto().apply {
                val classroomFound = classrooms.firstOrNull { c-> c.uuid == it.uuidClassroom }
                this.uuid = it.uuid
                this.uuidTeacher = uuid
                this.uuidClassroom = classroomFound?.uuid
                this.classroomName = classroomFound?.name
                this.gradeName = grades.firstOrNull { g-> g.uuid == classroomFound?.uuidGrade }?.name
            })
        }
        return final
    }

    override fun getByClassroom(uuid: UUID, period: Int): List<DirectorStudentDto> {
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(uuid)
        val directorStudents = directorStudentRepository.getAllByUuidClassroomStudentInAndPeriod(classroomStudents.mapNotNull { it.uuid }, period)
        val studentSubjectsInPeriod = studentSubjectRepository.findAllByUuidClassroomStudentIn(classroomStudents.mapNotNull { it.uuid }).filter { it.period == period }
        val users = userRepository.findAllById(classroomStudents.mapNotNull { it.uuidStudent }.distinct())
        val subjects = subjectRepository.findAllById(studentSubjectsInPeriod.mapNotNull { it.uuidSubject }.distinct())

        return classroomStudents.map { cs->
            val myDirectorStudent = directorStudents.firstOrNull { d-> d.uuidClassroomStudent == cs.uuid }
            val myUser = users.firstOrNull { u-> u.uuid == cs.uuidStudent }
            DirectorStudentDto().apply {
                this.description = myDirectorStudent?.description
                this.uuid = myDirectorStudent?.uuid
                this.name = myUser?.name
                this.lastname = myUser?.lastname
                this.period = period
                this.uuidStudent = cs.uuidStudent
                this.studentSubjects = studentSubjectsInPeriod.filter { ss-> ss.uuidClassroomStudent == cs.uuid }.map{ ss-> DirectorStudentSubjectDto().apply {
                    val subjectFound = subjects.firstOrNull { s-> s.uuid == ss.uuidSubject }
                    this.name = subjectFound?.name
                    this.uuid = subjectFound?.uuid
                    this.def = ss.def
                    this.recovery = ss.recovery
                    this.judgment = ss.judgment
                } }
            }
        }
    }

}
