package com.osia.nota_maestro.service.mesh

import com.osia.nota_maestro.dto.mesh.v1.MeshDto
import com.osia.nota_maestro.dto.mesh.v1.MeshRequest
import com.osia.nota_maestro.model.Mesh
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface MeshService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Mesh
    fun findByMultiple(uuidList: List<UUID>): List<MeshDto>
    fun findAll(pageable: Pageable, school: UUID): Page<MeshDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<MeshDto>
    // Create
    fun save(meshRequest: MeshRequest, replace: Boolean = false): MeshDto
    fun saveMultiple(meshRequestList: List<MeshRequest>): List<MeshDto>
    // Update
    fun update(uuid: UUID, meshRequest: MeshRequest, includeDelete: Boolean = false): MeshDto
    fun updateMultiple(meshDtoList: List<MeshDto>, classroom: UUID, subject: UUID, period: Int): List<MeshDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
    fun getBy(classroom: UUID, subject: UUID, period: Int): Mesh
    fun getByTeacher(classroom: UUID, teacher: UUID, period: Int): Mesh
    fun getByStudent(uuid: UUID, subject: UUID, period: Int): MeshDto
}
