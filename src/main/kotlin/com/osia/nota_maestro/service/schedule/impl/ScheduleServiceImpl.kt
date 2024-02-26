package com.osia.nota_maestro.service.schedule.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.schedule.v1.ScheduleComplete
import com.osia.nota_maestro.dto.schedule.v1.ScheduleDto
import com.osia.nota_maestro.dto.schedule.v1.ScheduleMapper
import com.osia.nota_maestro.dto.schedule.v1.SchedulePerHourDto
import com.osia.nota_maestro.dto.schedule.v1.ScheduleRequest
import com.osia.nota_maestro.model.Schedule
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.grade.GradeRepository
import com.osia.nota_maestro.repository.gradeSubject.GradeSubjectRepository
import com.osia.nota_maestro.repository.schedule.ScheduleRepository
import com.osia.nota_maestro.repository.subject.SubjectRepository
import com.osia.nota_maestro.service.schedule.ScheduleService
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

@Service("schedule.crud_service")
@Transactional
class ScheduleServiceImpl(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleMapper: ScheduleMapper,
    private val objectMapper: ObjectMapper,
    private val subjectRepository: SubjectRepository,
    private val gradeSubjectRepository: GradeSubjectRepository,
    private val gradeRepository: GradeRepository,
    private val classroomRepository: ClassroomRepository
) : ScheduleService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("schedule count -> increment: $increment")
        return scheduleRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Schedule {
        return scheduleRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Schedule $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ScheduleDto> {
        log.trace("schedule findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return scheduleRepository.findAllById(uuidList).map(scheduleMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ScheduleDto> {
        log.trace("schedule findAll -> pageable: $pageable")
        return scheduleRepository.findAll(Specification.where(CreateSpec<Schedule>().createSpec("", school)), pageable).map(scheduleMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ScheduleDto> {
        log.trace("schedule findAllByFilter -> pageable: $pageable, where: $where")
        return scheduleRepository.findAll(Specification.where(CreateSpec<Schedule>().createSpec(where, school)), pageable).map(scheduleMapper::toDto)
    }

    @Transactional
    override fun save(scheduleRequest: ScheduleRequest, replace: Boolean): ScheduleDto {
        log.trace("schedule save -> request: $scheduleRequest")
        val savedSchedule = scheduleMapper.toModel(scheduleRequest)
        return scheduleMapper.toDto(scheduleRepository.save(savedSchedule))
    }

    @Transactional
    override fun saveMultiple(scheduleRequestList: List<ScheduleRequest>): List<ScheduleDto> {
        log.trace("schedule saveMultiple -> requestList: ${objectMapper.writeValueAsString(scheduleRequestList)}")
        val schedules = scheduleRequestList.map(scheduleMapper::toModel)
        return scheduleRepository.saveAll(schedules).map(scheduleMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, scheduleRequest: ScheduleRequest, includeDelete: Boolean): ScheduleDto {
        log.trace("schedule update -> uuid: $uuid, request: $scheduleRequest")
        val schedule = if (!includeDelete) {
            getById(uuid)
        } else {
            scheduleRepository.getByUuid(uuid).get()
        }
        scheduleMapper.update(scheduleRequest, schedule)
        return scheduleMapper.toDto(scheduleRepository.save(schedule))
    }

    @Transactional
    override fun updateMultiple(scheduleDtoList: List<ScheduleDto>): List<ScheduleDto> {
        log.trace("schedule updateMultiple -> scheduleDtoList: ${objectMapper.writeValueAsString(scheduleDtoList)}")
        val schedules = scheduleRepository.findAllById(scheduleDtoList.mapNotNull { it.uuid })
        schedules.forEach { schedule ->
            scheduleMapper.update(scheduleMapper.toRequest(scheduleDtoList.first { it.uuid == schedule.uuid }), schedule)
        }
        return scheduleRepository.saveAll(schedules).map(scheduleMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("schedule delete -> uuid: $uuid")
        val schedule = getById(uuid)
        schedule.deleted = true
        schedule.deletedAt = LocalDateTime.now()
        scheduleRepository.save(schedule)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("schedule deleteMultiple -> uuid: $uuidList")
        val schedules = scheduleRepository.findAllById(uuidList)
        schedules.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        scheduleRepository.saveAll(schedules)
    }

    override fun getCompleteSchedule(school: UUID, classroom: UUID): ScheduleComplete {
        log.trace("schedule getCompleteSchedule -> uuid: $school")
        val classR = classroomRepository.findById(classroom).
        orElseThrow{ throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not found") }
        val grade = gradeRepository.findById(classR.uuidGrade!!).
        orElseThrow{ throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not found") }

        val schedules = scheduleRepository.findAllByUuidSchoolAndUuidClassroom(school, classroom)
        val gradeSubjects = gradeSubjectRepository.findAllById(schedules.mapNotNull { it.uuidGradeSubject })
        val subjects = subjectRepository.findAllById(gradeSubjects.mapNotNull { it.uuidSubject })
        val group = schedules.sortedBy { it.dayOfWeek }.groupBy { it.init }
        return ScheduleComplete().apply {
            this.duration = grade.duration
            this.hourFinish = grade.hourFinish
            this.hourInit = grade.hourInit
            this.recessInit = grade.recessInit
            this.recessFinish = grade.recessFinish
            this.recessaInit = grade.recessaInit
            this.recessaFinish = grade.recessaFinish
            this.recess = grade.recess
            this.hours = group.map { (k, gr)-> SchedulePerHourDto().apply {
                this.schedules = gr.map(scheduleMapper::toDto)
                this.schedules?.forEach {
                    val gs = gradeSubjects.firstOrNull { g-> g.uuid == it.uuidGradeSubject }
                    val sx = subjects.firstOrNull { s-> s.uuid == gs?.uuidSubject }
                    it.uuidSubject = sx?.uuid
                    it.subjectName = sx?.name
                }
                this.init = gr.firstOrNull()?.init
                this.finish = gr.firstOrNull()?.finish
            } }
        }
    }
}
