package com.osia.nota_maestro.service.attendance

import com.osia.nota_maestro.dto.attendance.v1.AttendanceCompleteDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceDto
import com.osia.nota_maestro.dto.attendance.v1.AttendanceRequest
import com.osia.nota_maestro.dto.attendanceFail.v1.AttendanceStudentDto
import com.osia.nota_maestro.dto.resources.v1.ResourceGradeDto
import com.osia.nota_maestro.dto.resources.v1.ResourceSubjectDto
import com.osia.nota_maestro.model.Attendance
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AttendanceService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Attendance
    fun findByMultiple(uuidList: List<UUID>): List<AttendanceDto>
    fun findAll(pageable: Pageable, school: UUID): Page<AttendanceDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AttendanceDto>
    // Create
    fun save(attendanceRequest: AttendanceRequest, replace: Boolean = false): AttendanceDto
    fun saveMultiple(attendanceRequestList: List<AttendanceRequest>): List<AttendanceDto>
    // Update
    fun update(uuid: UUID, attendanceRequest: AttendanceRequest, includeDelete: Boolean = false): AttendanceDto
    fun updateMultiple(attendanceDtoList: List<AttendanceDto>): List<AttendanceDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun getByStudent(uuid: UUID, subject: UUID?, month: Int): List<List<AttendanceStudentDto>>
    fun getByStudentNull(uuid: UUID, month: Int): List<List<AttendanceStudentDto>>

    fun getComplete(classroom: UUID, subject: UUID, month: Int, school: UUID): List<AttendanceCompleteDto>
    fun getCompleteGroup(classroom: UUID, month: Int, school: UUID): List<AttendanceCompleteDto>
    fun submit(classroom: UUID, subject: UUID, month: Int, school: UUID, req: List<AttendanceCompleteDto>): List<AttendanceCompleteDto>
    fun submitGroup(classroom: UUID, month: Int, school: UUID, req: List<AttendanceCompleteDto>): List<AttendanceCompleteDto>

    fun getResources(uuid: UUID): List<ResourceGradeDto>
    fun getResourcesStudent(uuid: UUID): List<ResourceSubjectDto>
}
