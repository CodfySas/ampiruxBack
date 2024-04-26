package com.osia.nota_maestro.service.planning.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.notification.v1.NotificationDto
import com.osia.nota_maestro.dto.planning.v1.PlanningDto
import com.osia.nota_maestro.dto.planning.v1.PlanningMapper
import com.osia.nota_maestro.dto.planning.v1.PlanningRequest
import com.osia.nota_maestro.model.Planning
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.planning.PlanningRepository
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.planning.PlanningService
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

@Service("planning.crud_service")
@Transactional
class PlanningServiceImpl(
    private val planningRepository: PlanningRepository,
    private val planningMapper: PlanningMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolRepository: SchoolRepository
) : PlanningService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("planning count -> increment: $increment")
        return planningRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Planning {
        return planningRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Planning $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<PlanningDto> {
        log.trace("planning findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return planningRepository.findAllById(uuidList).map(planningMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<PlanningDto> {
        log.trace("planning findAll -> pageable: $pageable")
        return planningRepository.findAll(Specification.where(CreateSpec<Planning>().createSpec("", school)), pageable).map(planningMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<PlanningDto> {
        log.trace("planning findAllByFilter -> pageable: $pageable, where: $where")
        return planningRepository.findAll(Specification.where(CreateSpec<Planning>().createSpec(where, null)), pageable).map(planningMapper::toDto)
    }

    @Transactional
    override fun save(planningRequest: PlanningRequest, school: UUID, replace: Boolean): PlanningDto {
        log.trace("planning save -> request: $planningRequest")
        val sf = schoolRepository.findById(school).orElseThrow{
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val found = if(sf.planningType != "group"){
            planningRepository.findFirstByClassroomAndSubjectAndWeek(planningRequest.classroom!!, planningRequest.subject!!, planningRequest.week!!)
        }else{
            planningRepository.findFirstByClassroomAndUuidTeacherAndWeek(planningRequest.classroom!!, planningRequest.uuidTeacher!!, planningRequest.week!!)
        }
        return if(found.isPresent){
            update(found.get().uuid!!, planningRequest)
        }else{
            planningMapper.toDto(planningRepository.save(planningMapper.toModel(planningRequest)))
        }
    }

    @Transactional
    override fun saveMultiple(planningRequestList: List<PlanningRequest>): List<PlanningDto> {
        log.trace("planning saveMultiple -> requestList: ${objectMapper.writeValueAsString(planningRequestList)}")
        val plannings = planningRequestList.map(planningMapper::toModel)
        return planningRepository.saveAll(plannings).map(planningMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, planningRequest: PlanningRequest, includeDelete: Boolean): PlanningDto {
        log.trace("planning update -> uuid: $uuid, request: $planningRequest")
        val planning = if (!includeDelete) {
            getById(uuid)
        } else {
            planningRepository.getByUuid(uuid).get()
        }
        planningMapper.update(planningRequest, planning)
        return planningMapper.toDto(planningRepository.save(planning))
    }

    @Transactional
    override fun updateMultiple(planningRequest: List<PlanningDto>): List<PlanningDto> {
        log.trace(
            "planning updateMultiple -> planningDtoList: ${
                objectMapper.writeValueAsString(
                    planningRequest
                )
            }"
        )
        val plannings = planningRepository.findAllById(planningRequest.mapNotNull { it.uuid })
        plannings.forEach { planning ->
            planningMapper.update(
                planningMapper.toRequest(planningRequest.first { it.uuid == planning.uuid }),
                planning
            )
        }
        return planningRepository.saveAll(plannings).map(planningMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("planning delete -> uuid: $uuid")
        val planning = getById(uuid)
        planning.deleted = true
        planning.deletedAt = LocalDateTime.now()
        planningRepository.save(planning)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("planning deleteMultiple -> uuid: $uuidList")
        val plannings = planningRepository.findAllById(uuidList)
        plannings.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        planningRepository.saveAll(plannings)
    }

    override fun getBy(classroom: UUID, subject: UUID, week: Int): Planning {
       return planningRepository.findFirstByClassroomAndSubjectAndWeek(classroom, subject, week).orElse(Planning())
    }

    override fun getByTeacher(classroom: UUID, teacher: UUID, week: Int): Planning {
        return planningRepository.findFirstByClassroomAndUuidTeacherAndWeek(classroom, teacher, week).orElse(Planning())
    }
}
