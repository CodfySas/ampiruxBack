package com.osia.nota_maestro.service.attendanceFail

import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailDto
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceFailRequest
import com.osia.nota_maestro.model.AttendanceFail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AttendanceFailService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): AttendanceFail
    fun findByMultiple(uuidList: List<UUID>): List<AttendanceFailDto>
    fun findAll(pageable: Pageable, school: UUID): Page<AttendanceFailDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AttendanceFailDto>
    // Create
    fun save(attendanceFailRequest: AttendanceFailRequest, replace: Boolean = false): AttendanceFailDto
    fun saveMultiple(attendanceFailRequestList: List<AttendanceFailRequest>): List<AttendanceFailDto>
    // Update
    fun update(uuid: UUID, attendanceFailRequest: AttendanceFailRequest, includeDelete: Boolean = false): AttendanceFailDto
    fun updateMultiple(attendanceFailDtoList: List<AttendanceFailDto>): List<AttendanceFailDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
