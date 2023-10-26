package com.osia.nota_maestro.service.worker

import com.osia.nota_maestro.dto.worker.v1.WorkerDto
import com.osia.nota_maestro.dto.worker.v1.WorkerRequest
import com.osia.nota_maestro.model.Worker
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface WorkerService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Worker
    fun findByMultiple(uuidList: List<UUID>): List<WorkerDto>
    fun findAll(pageable: Pageable): Page<WorkerDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<WorkerDto>
    // Create
    fun save(workerRequest: WorkerRequest): WorkerDto
    fun saveMultiple(workerRequestList: List<WorkerRequest>): List<WorkerDto>
    // Update
    fun update(uuid: UUID, workerRequest: WorkerRequest): WorkerDto
    fun updateMultiple(workerDtoList: List<WorkerDto>): List<WorkerDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
