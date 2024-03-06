package com.osia.nota_maestro.service.accompaniment

import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentCompleteDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentRequest
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.model.Accompaniment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface AccompanimentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Accompaniment
    fun findByMultiple(uuidList: List<UUID>): List<AccompanimentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<AccompanimentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<AccompanimentDto>
    // Create
    fun save(accompanimentRequest: AccompanimentRequest, replace: Boolean = false): AccompanimentDto
    fun saveMultiple(accompanimentRequestList: List<AccompanimentRequest>): List<AccompanimentDto>
    // Update
    fun update(uuid: UUID, accompanimentRequest: AccompanimentRequest, includeDelete: Boolean = false): AccompanimentDto
    fun updateMultiple(accompanimentDtoList: List<AccompanimentDto>): List<AccompanimentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getComplete(school: UUID): List<AccompanimentCompleteDto>
    fun saveComplete(complete: List<AccompanimentCompleteDto>, school: UUID): List<AccompanimentCompleteDto>

    fun getMyGroups(uuid: UUID, school: UUID): List<AccompanimentCompleteDto>
    fun getByClassroom(uuid: UUID, period: Int): List<AccompanimentStudentDto>
    fun submit(uuid: UUID, period: Int, req: List<AccompanimentStudentDto>): List<AccompanimentStudentDto>
}
