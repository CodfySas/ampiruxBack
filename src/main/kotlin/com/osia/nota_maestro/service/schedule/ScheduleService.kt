package com.osia.nota_maestro.service.schedule

import com.osia.nota_maestro.dto.schedule.v1.ScheduleComplete
import com.osia.nota_maestro.dto.schedule.v1.ScheduleDto
import com.osia.nota_maestro.dto.schedule.v1.ScheduleRequest
import com.osia.nota_maestro.model.Schedule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ScheduleService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Schedule
    fun findByMultiple(uuidList: List<UUID>): List<ScheduleDto>
    fun findAll(pageable: Pageable, school: UUID): Page<ScheduleDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ScheduleDto>
    // Create
    fun save(scheduleRequest: ScheduleRequest, replace: Boolean = false): ScheduleDto
    fun saveMultiple(scheduleRequestList: List<ScheduleRequest>): List<ScheduleDto>
    // Update
    fun update(uuid: UUID, scheduleRequest: ScheduleRequest, includeDelete: Boolean = false): ScheduleDto
    fun updateMultiple(scheduleDtoList: List<ScheduleDto>): List<ScheduleDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getCompleteSchedule(school: UUID, classroom: UUID): ScheduleComplete
}
