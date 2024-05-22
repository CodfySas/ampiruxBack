package com.osia.nota_maestro.service.planning

import com.osia.nota_maestro.dto.planning.v1.PlanningDto
import com.osia.nota_maestro.dto.planning.v1.PlanningRequest
import com.osia.nota_maestro.model.Planning
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PlanningService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Planning
    fun findByMultiple(uuidList: List<UUID>): List<PlanningDto>
    fun findAll(pageable: Pageable, school: UUID): Page<PlanningDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<PlanningDto>
    // Create
    fun save(planningRequest: PlanningRequest, school: UUID, replace: Boolean = false): PlanningDto
    fun saveMultiple(planningRequestList: List<PlanningRequest>): List<PlanningDto>
    // Update
    fun update(uuid: UUID, planningRequest: PlanningRequest, includeDelete: Boolean = false): PlanningDto
    fun updateMultiple(planningRequest: List<PlanningDto>): List<PlanningDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getBy(classroom: UUID, subject: UUID, week: Int): Planning
    fun getByTeacher(classroom: UUID, teacher: UUID, week: Int): Planning
}
