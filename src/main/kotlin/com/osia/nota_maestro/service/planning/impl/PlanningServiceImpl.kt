package com.osia.nota_maestro.service.planning.impl

import com.fasterxml.jackson.databind.ObjectMapper
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
    override fun save(planningRequest: PlanningRequest, replace: Boolean): PlanningDto {
        log.trace("planning save -> request: $planningRequest")
        val savedPlanning = planningMapper.toModel(planningRequest)
        return planningMapper.toDto(planningRepository.save(savedPlanning))
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
    override fun updateMultiple(planningDtoList: List<PlanningDto>, classroom: UUID, subject: UUID, period: Int): List<PlanningDto> {
        log.trace("planning updateMultiple -> planningDtoList: ${objectMapper.writeValueAsString(planningDtoList)}")
        val getBy = getBy(classroom, subject, period)
        val toSave = planningDtoList.filter { it.uuid == null }
        val toUpdate = planningDtoList.filter { it.uuid != null }
        val toDelete = getBy.filterNot { m-> planningDtoList.mapNotNull { it.uuid }.contains(m.uuid) }.mapNotNull { it.uuid }
        val toUpdateAll = mutableListOf<Planning>()
        toUpdate.forEach {
            val foundSaved = getBy.first { f-> f.uuid == it.uuid }
            planningMapper.update(planningMapper.toRequest(it), foundSaved)
            toUpdateAll.add(foundSaved)
        }
        deleteMultiple(toDelete)
        planningRepository.saveAll(toUpdateAll)
        return saveMultiple(toSave.map(planningMapper::toRequest))
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

    override fun getBy(classroom: UUID, subject: UUID, week: Int): List<Planning> {
       return planningRepository.findAllByClassroomAndSubjectAndWeek(classroom, subject, week)
    }

    override fun getByMy(uuid: UUID, subject: UUID, week: Int): List<PlanningDto> {
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
        return getBy(classroomStudent.uuidClassroom!!, subject, week).map(planningMapper::toDto).sortedBy { it.position }
    }
}
