package com.osia.nota_maestro.service.log

import com.osia.nota_maestro.dto.log.v1.LogDto
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.model.Log
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface LogService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Log
    fun findByMultiple(uuidList: List<UUID>): List<LogDto>
    fun findAll(pageable: Pageable, school: UUID): Page<LogDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<LogDto>
    // Create
    fun save(logRequest: LogRequest, replace: Boolean = false): LogDto
    fun saveMultiple(logRequestList: List<LogRequest>): List<LogDto>
    // Update
    fun update(uuid: UUID, logRequest: LogRequest, includeDelete: Boolean = false): LogDto
    fun updateMultiple(logDtoList: List<LogDto>): List<LogDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun findAllByMonth(month: Int, day: Int, school: UUID): List<LogDto>
}
