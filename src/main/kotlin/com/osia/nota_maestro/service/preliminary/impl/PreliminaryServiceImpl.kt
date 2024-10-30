package com.osia.nota_maestro.service.preliminary.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryAllRequest
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryDto
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryMapper
import com.osia.nota_maestro.dto.preliminary.v1.PreliminaryRequest
import com.osia.nota_maestro.model.Preliminary
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.classroomSubject.ClassroomSubjectRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.preliminary.PreliminaryRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.preliminary.PreliminaryService
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

@Service("preliminary.crud_service")
@Transactional
class PreliminaryServiceImpl(
    private val preliminaryRepository: PreliminaryRepository,
    private val preliminaryMapper: PreliminaryMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository,
    private val classroomSubjectRepository: ClassroomSubjectRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val gradeRepository: GradeRepository,
    private val classroomRepository: ClassroomRepository
) : PreliminaryService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("preliminary count -> increment: $increment")
        return preliminaryRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Preliminary {
        return preliminaryRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Preliminary $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<PreliminaryDto> {
        log.trace("preliminary findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return preliminaryRepository.findAllById(uuidList).map(preliminaryMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<PreliminaryDto> {
        log.trace("preliminary findAll -> pageable: $pageable")
        return preliminaryRepository.findAll(Specification.where(CreateSpec<Preliminary>().createSpec("", school)), pageable).map(preliminaryMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PreliminaryDto> {
        log.trace("preliminary findAllByFilter -> pageable: $pageable, where: $where")
        return preliminaryRepository.findAll(Specification.where(CreateSpec<Preliminary>().createSpec(where, school)), pageable).map(preliminaryMapper::toDto)
    }

    @Transactional
    override fun save(preliminaryRequest: PreliminaryRequest, replace: Boolean): PreliminaryDto {
        log.trace("preliminary save -> request: $preliminaryRequest")
        val savedPreliminary = preliminaryMapper.toModel(preliminaryRequest)
        return preliminaryMapper.toDto(preliminaryRepository.save(savedPreliminary))
    }

    @Transactional
    override fun saveMultiple(preliminaryRequestList: List<PreliminaryRequest>): List<PreliminaryDto> {
        log.trace("preliminary saveMultiple -> requestList: ${objectMapper.writeValueAsString(preliminaryRequestList)}")
        val preliminaries = preliminaryRequestList.map(preliminaryMapper::toModel)
        return preliminaryRepository.saveAll(preliminaries).map(preliminaryMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, preliminaryRequest: PreliminaryRequest, includeDelete: Boolean): PreliminaryDto {
        log.trace("preliminary update -> uuid: $uuid, request: $preliminaryRequest")
        val preliminary = if (!includeDelete) {
            getById(uuid)
        } else {
            preliminaryRepository.getByUuid(uuid).get()
        }
        preliminaryMapper.update(preliminaryRequest, preliminary)
        return preliminaryMapper.toDto(preliminaryRepository.save(preliminary))
    }

    @Transactional
    override fun updateMultiple(preliminaryDtoList: List<PreliminaryDto>): List<PreliminaryDto> {
        log.trace("preliminary updateMultiple -> preliminaryDtoList: ${objectMapper.writeValueAsString(preliminaryDtoList)}")
        val preliminaries = preliminaryRepository.findAllById(preliminaryDtoList.mapNotNull { it.uuid })
        preliminaries.forEach { preliminary ->
            preliminaryMapper.update(preliminaryMapper.toRequest(preliminaryDtoList.first { it.uuid == preliminary.uuid }), preliminary)
        }
        return preliminaryRepository.saveAll(preliminaries).map(preliminaryMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("preliminary delete -> uuid: $uuid")
        val preliminary = getById(uuid)
        preliminary.deleted = true
        preliminary.deletedAt = LocalDateTime.now()
        preliminaryRepository.save(preliminary)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("preliminary deleteMultiple -> uuid: $uuidList")
        val preliminaries = preliminaryRepository.findAllById(uuidList)
        preliminaries.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        preliminaryRepository.saveAll(preliminaries)
    }

    override fun getByClassroom(preliminaryReq: PreliminaryAllRequest, user: UUID): List<PreliminaryAllDto> {
        val result = mutableListOf<PreliminaryAllDto>()
        val classroomStudents = classroomStudentRepository.findAllByUuidClassroom(preliminaryReq.classroom!!)
        val students = userRepository.getAllByUuidIn(classroomStudents.mapNotNull { it.uuidStudent })
        val preliminaryList = preliminaryRepository.findAllByUuidClassroomAndPeriod(preliminaryReq.classroom!!, preliminaryReq.period!!)
        val classroomSubjects = classroomSubjectRepository.getAllByUuidClassroom(preliminaryReq.classroom!!)
        val userF = userRepository.getByUuid(user)
        val myClassSub = if(userF.get().role == "admin"){
            classroomSubjects
        }else{
            classroomSubjects.filter { cs-> cs.uuidTeacher == userF.get().uuid}
        }
        val subjects = subjectRepository.findAllById(classroomSubjects.mapNotNull { it.uuidSubject }.distinct())
        students.forEach {
            val pf = preliminaryList.filter { p-> p.uuidStudent == it.uuid }
            result.add(PreliminaryAllDto().apply {
                this.uuidClassroom = preliminaryReq.classroom
                this.period = preliminaryReq.period
                this.uuidStudent = it.uuid
                this.student = it.name + " " + it.lastname

                val preList = mutableListOf<PreliminaryDto>()
                subjects.forEach { s->
                    val pff = pf.firstOrNull{ p-> p.uuidSubject == s.uuid }
                    preList.add(PreliminaryDto().apply {
                        this.uuid = pff?.uuid
                        this.aspect = pff?.aspect ?: ""
                        this.observations = pff?.observations ?: ""
                        this.subject = s.name ?: ""
                        this.success = pff?.success ?: false
                        this.target = pff?.target ?: ""
                        this.uuidSubject = s.uuid
                        val found = myClassSub.firstOrNull { m-> m.uuidSubject == s.uuid && m.uuidClassroom == preliminaryReq.classroom }
                        this.canEdit = (found != null)
                    })
                }
                this.preliminaries = preList
            })
        }
        return result
    }

    override fun submit(req: List<PreliminaryAllDto>, period: Int, classroom: UUID): List<PreliminaryAllDto> {
        val preliminaryList = preliminaryRepository.findAllByUuidClassroomAndPeriod(classroom, period)
        val toUpdate = mutableListOf<PreliminaryDto>()
        val toSave = mutableListOf<PreliminaryRequest>()
        req.forEach { s->
            s.preliminaries?.forEach { subject ->
                if(subject.canEdit == true){
                    if(subject.uuid != null){
                        val pFound = preliminaryList.firstOrNull { p-> p.uuid == subject.uuid }
                        if(pFound != null){
                            pFound.aspect = subject.aspect
                            pFound.observations = subject.observations
                            pFound.target = subject.target
                            pFound.success = subject.success
                            toUpdate.add(preliminaryMapper.toDto(pFound))
                        }
                    }else{
                        toSave.add(PreliminaryRequest().apply {
                            this.target = subject.target
                            this.success = subject.success
                            this.aspect = subject.aspect
                            this.observations = subject.observations
                            this.uuidStudent = s.uuidStudent
                            this.uuidClassroom = classroom
                            this.uuidSubject = subject.uuidSubject
                            this.period = period
                        })
                    }
                }
            }
        }
        saveMultiple(toSave)
        updateMultiple(toUpdate)
        return req
    }

    override fun repair() {
        val gradesGrouped = gradeRepository.findAll().filter { it.preInfoType == "grouped" }
        val classrooms = classroomRepository.findAllByUuidGradeIn(gradesGrouped.mapNotNull { it.uuid })

        classrooms.forEach { c->
            val allPreliminariesToRepair = preliminaryRepository.findAllByUuidClassroomAndPeriod(c.uuid!!, 3)
            allPreliminariesToRepair.forEach { p->
                if(p.target?.startsWith("[") == false){
                    p.target = "[{\"target\":\"${p.target}\",\"success\":${p.success}}]"
                }
            }
            preliminaryRepository.saveAll(allPreliminariesToRepair)
        }
    }
}
