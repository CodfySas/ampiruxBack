package com.osia.nota_maestro.service.classroomResource.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceDto
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceMapper
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceRequest
import com.osia.nota_maestro.dto.classroomResource.v1.ExamCompleteDto
import com.osia.nota_maestro.dto.examAttempt.v1.ExamAttemptRequest
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseDto
import com.osia.nota_maestro.dto.examResponse.v1.ExamResponseRequest
import com.osia.nota_maestro.dto.examUserResponse.v1.ExamUserResponseRequest
import com.osia.nota_maestro.model.ClassroomResource
import com.osia.nota_maestro.model.ExamQuestion
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomResource.ClassroomResourceRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.examQuestion.ExamQuestionRepository
import com.osia.nota_maestro.repository.examResponse.ExamResponseRepository
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroomResource.ClassroomResourceService
import com.osia.nota_maestro.service.examAttempt.ExamAttemptService
import com.osia.nota_maestro.service.examQuestion.ExamQuestionService
import com.osia.nota_maestro.service.examResponse.ExamResponseService
import com.osia.nota_maestro.service.examUserResponse.ExamUserResponseService
import com.osia.nota_maestro.util.CreateSpec
import com.osia.nota_maestro.util.SubmitFile
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.UUID

@Service("classroomResource.crud_service")
@Transactional
class ClassroomResourceServiceImpl(
    private val classroomResourceRepository: ClassroomResourceRepository,
    private val classroomResourceMapper: ClassroomResourceMapper,
    private val objectMapper: ObjectMapper,
    private val classroomRepository: ClassroomRepository,
    private val schoolRepository: SchoolRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val userRepository: UserRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val examQuestionRepository: ExamQuestionRepository,
    private val examQuestionService: ExamQuestionService,
    private val examResponseRepository: ExamResponseRepository,
    private val examResponseService: ExamResponseService,
    private val examAttemptService: ExamAttemptService,
    private val examUserResponseService: ExamUserResponseService
) : ClassroomResourceService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroomResource count -> increment: $increment")
        return classroomResourceRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ClassroomResource {
        return classroomResourceRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ClassroomResource $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomResourceDto> {
        log.trace("classroomResource findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomResourceRepository.findAllById(uuidList).map(classroomResourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomResourceDto> {
        log.trace("classroomResource findAll -> pageable: $pageable")
        return classroomResourceRepository.findAll(Specification.where(CreateSpec<ClassroomResource>().createSpec("", school)), pageable).map(classroomResourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomResourceDto> {
        log.trace("classroomResource findAllByFilter -> pageable: $pageable, where: $where")
        return classroomResourceRepository.findAll(Specification.where(CreateSpec<ClassroomResource>().createSpec(where, school)), pageable).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun save(classroomResourceRequest: ClassroomResourceRequest, replace: Boolean): ClassroomResourceDto {
        log.trace("classroomResource save -> request: $classroomResourceRequest")
        val savedClassroomResource = classroomResourceMapper.toModel(classroomResourceRequest)
        return classroomResourceMapper.toDto(classroomResourceRepository.save(savedClassroomResource))
    }

    @Transactional
    override fun saveMultiple(classroomResourceRequestList: List<ClassroomResourceRequest>): List<ClassroomResourceDto> {
        log.trace("classroomResource saveMultiple -> requestList: ${objectMapper.writeValueAsString(classroomResourceRequestList)}")
        val classroomResources = classroomResourceRequestList.map(classroomResourceMapper::toModel)
        return classroomResourceRepository.saveAll(classroomResources).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, classroomResourceRequest: ClassroomResourceRequest, includeDelete: Boolean): ClassroomResourceDto {
        log.trace("classroomResource update -> uuid: $uuid, request: $classroomResourceRequest")
        val classroomResource = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomResourceRepository.getByUuid(uuid).get()
        }
        classroomResourceMapper.update(classroomResourceRequest, classroomResource)
        return classroomResourceMapper.toDto(classroomResourceRepository.save(classroomResource))
    }

    @Transactional
    override fun updateMultiple(classroomResourceDtoList: List<ClassroomResourceDto>): List<ClassroomResourceDto> {
        log.trace("classroomResource updateMultiple -> classroomResourceDtoList: ${objectMapper.writeValueAsString(classroomResourceDtoList)}")
        val classroomResources = classroomResourceRepository.findAllById(classroomResourceDtoList.mapNotNull { it.uuid })
        classroomResources.forEach { classroomResource ->
            classroomResourceMapper.update(classroomResourceMapper.toRequest(classroomResourceDtoList.first { it.uuid == classroomResource.uuid }), classroomResource)
        }
        return classroomResourceRepository.saveAll(classroomResources).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroomResource delete -> uuid: $uuid")
        val classroomResource = getById(uuid)
        classroomResource.deleted = true
        classroomResource.deletedAt = LocalDateTime.now()
        classroomResourceRepository.save(classroomResource)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroomResource deleteMultiple -> uuid: $uuidList")
        val classroomResources = classroomResourceRepository.findAllById(uuidList)
        classroomResources.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomResourceRepository.saveAll(classroomResources)
    }

    override fun getBy(classroom: UUID, subject: UUID): List<List<ClassroomResourceDto>> {
        val classroomF = classroomRepository.getByUuid(classroom).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolPeriods = schoolPeriodRepository.findAllByUuidSchool(classroomF.uuidSchool!!)
        val resources = classroomResourceRepository.findAllByClassroomAndSubject(classroom, subject).sortedBy { it.createdAt }
        val finalList = mutableListOf<List<ClassroomResourceDto>>()
        schoolPeriods.forEach {
            val rsP = resources.filter { rs -> rs.period == it.number }
            finalList.add(rsP.map(classroomResourceMapper::toDto))
        }
        return finalList
    }

    override fun getByMy(uuid: UUID, subject: UUID): List<List<ClassroomResourceDto>> {
        val user = userRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val school = schoolRepository.findById(user.uuidSchool!!).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val classrooms = classroomRepository.findAllByUuidSchoolAndYear(user.uuidSchool!!, school.actualYear!!)
        val classroomStudent = classroomStudentRepository.findFirstByUuidClassroomInAndUuidStudent(classrooms.mapNotNull { it.uuid }.distinct(), uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        return getBy(classroomStudent.uuidClassroom!!, subject)
    }

    override fun download(uuid: UUID): ResponseEntity<ByteArray> {
        val resource = getById(uuid)
        return try {
            val targetLocation: Path = Path.of("src/main/resources/files/$uuid.${resource.ext}")
            val fileBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(SubmitFile().determineMediaType(resource.ext ?: "")).body(fileBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }

    override fun getCompleteExamByTeacher(uuid: UUID, task: UUID, showResponse: Boolean): ExamCompleteDto {
        val user = userRepository.getByUuid(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        if (user.role == "student" && showResponse) {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "trying to hack? :)")
        }
        val exam = classroomResourceRepository.getByUuid(task).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val allExamQuestions = examQuestionRepository.getAllByUuidExam(exam.uuid!!)
        val allResponses = examResponseRepository.getAllByUuidExamQuestionIn(allExamQuestions.mapNotNull { it.uuid })

        return ExamCompleteDto().apply {
            this.uuid = exam.uuid
            this.attempts = exam.attempts
            this.lastHour = exam.lastHour
            this.finishTime = exam.finishTime
            this.durationTime = exam.durationTime
            this.initHour = exam.initHour
            this.initTime = exam.initTime
            this.name = exam.name
            this.questions = allExamQuestions.filter { it.uuidExam == exam.uuid }.map {
                ExamQuestionDto().apply {
                    this.uuid = it.uuid
                    this.ordered = it.ordered
                    this.description = it.description
                    this.type = it.type
                    this.responses = allResponses.filter { r -> r.uuidExamQuestion == it.uuid }.map { r ->
                        ExamResponseDto().apply {
                            this.uuid = r.uuid
                            this.description = r.description
                            if (showResponse) {
                                this.correct = r.correct
                            }
                        }
                    }
                }
            }
        }
    }

    @Transactional
    override fun submitExam(exam: ExamCompleteDto, classroom: UUID, subject: UUID, period: Int): ExamCompleteDto {
        val req = ClassroomResourceRequest().apply {
            this.attempts = exam.attempts
            this.lastHour = exam.lastHour
            this.finishTime = exam.finishTime
            this.initHour = exam.initHour
            this.initTime = exam.initTime
            this.durationTime = exam.durationTime
            this.name = exam.name
        }

        val examFound = if (exam.uuid == null) {
            save(
                req.apply {
                    this.classroom = classroom
                    this.period = period
                    this.subject = subject
                    this.hasFile = false
                    this.type = "exam"
                }
            )
        } else {
            val found = getById(exam.uuid!!)
            update(found.uuid!!, req)
        }

        val toCreate = mutableListOf<ExamQuestion>()
        val toUpdate = mutableListOf<ExamQuestionDto>()
        val allExamQuestions = examQuestionRepository.getAllByUuidExam(examFound.uuid!!)
        val submit = exam.questions ?: mutableListOf()
        val toDelete = allExamQuestions.filterNot { submit.mapNotNull { r -> r.uuid }.contains(it.uuid!!) }

        val toCreateR = mutableListOf<ExamResponseRequest>()
        val toUpdateR = mutableListOf<ExamResponseDto>()
        val allResponses = examResponseRepository.getAllByUuidExamQuestionIn(submit.mapNotNull { it.uuid })
        val submitR = submit.flatMap { it.responses ?: mutableListOf() }
        val toDeleteR = allResponses.filterNot { submitR.mapNotNull { r -> r.uuid }.contains(it.uuid!!) }

        exam.questions?.forEach {
            var newUuid = UUID.randomUUID()
            if (it.uuid == null) {
                toCreate.add(
                    ExamQuestion().apply {
                        this.uuid = newUuid
                        this.uuidExam = examFound.uuid!!
                        this.type = it.type
                        this.description = it.description
                        this.ordered = it.ordered
                    }
                )
            } else {
                newUuid = it.uuid
                toUpdate.add(it)
            }
            it.responses?.forEach { r ->
                if (r.uuid == null) {
                    toCreateR.add(
                        ExamResponseRequest().apply {
                            this.uuidExamQuestion = newUuid
                            this.description = r.description
                            this.correct = r.correct
                        }
                    )
                } else {
                    toUpdateR.add(r)
                }
            }
        }

        examQuestionRepository.saveAll(toCreate)
        examQuestionService.updateMultiple(toUpdate)
        examQuestionService.deleteMultiple(toDelete.mapNotNull { it.uuid })

        examResponseService.saveMultiple(toCreateR)
        examResponseService.updateMultiple(toUpdateR)
        examResponseService.deleteMultiple(toDeleteR.mapNotNull { it.uuid })

        return exam
    }

    @Transactional
    override fun submitAttempt(uuid: UUID, exam: UUID, responses: List<ExamQuestionDto>): List<ExamQuestionDto> {
        val examAttempt = examAttemptService.save(
            ExamAttemptRequest().apply {
                this.uuidExam = exam
                this.uuidStudent = uuid
            }
        )
        val responsesToSave = mutableListOf<ExamUserResponseRequest>()
        responses.forEach {
            responsesToSave.add(
                ExamUserResponseRequest().apply {
                    this.uuidAttempt = examAttempt.uuid
                    this.uuidExamQuestion = it.uuid
                    this.response = it.responseOpen
                    this.uuidExamResponse = it.responses?.firstOrNull { r -> r.selected == true }?.uuid
                }
            )
        }
        examUserResponseService.saveMultiple(responsesToSave)
        return responses
    }
}
