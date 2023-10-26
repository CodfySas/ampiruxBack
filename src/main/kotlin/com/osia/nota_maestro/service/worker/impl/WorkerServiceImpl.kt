package com.osia.nota_maestro.service.worker.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.worker.v1.WorkerDto
import com.osia.nota_maestro.dto.worker.v1.WorkerMapper
import com.osia.nota_maestro.dto.worker.v1.WorkerRequest
import com.osia.nota_maestro.model.Worker
import com.osia.nota_maestro.repository.worker.WorkerRepository
import com.osia.nota_maestro.service.worker.WorkerService
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
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("worker.crud_service")
@Transactional
class WorkerServiceImpl(
    private val workerRepository: WorkerRepository,
    private val workerMapper: WorkerMapper,
    private val objectMapper: ObjectMapper
) : WorkerService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("worker count -> increment: $increment")
        return workerRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Worker {
        return workerRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Worker $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<WorkerDto> {
        log.trace("worker findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return workerRepository.findAllById(uuidList).map(workerMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<WorkerDto> {
        log.trace("worker findAll -> pageable: $pageable")
        return workerRepository.findAll(pageable).map(workerMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<WorkerDto> {
        log.trace("worker findAllByFilter -> pageable: $pageable, where: $where")
        return workerRepository.findAll(Specification.where(createSpec(where)), pageable).map(workerMapper::toDto)
    }

    @Transactional
    override fun save(workerRequest: WorkerRequest): WorkerDto {
        log.trace("worker save -> request: $workerRequest")
        val worker = workerMapper.toModel(workerRequest)
        return workerMapper.toDto(workerRepository.save(worker))
    }

    @Transactional
    override fun saveMultiple(workerRequestList: List<WorkerRequest>): List<WorkerDto> {
        log.trace("worker saveMultiple -> requestList: ${objectMapper.writeValueAsString(workerRequestList)}")
        val workers = workerRequestList.map(workerMapper::toModel)
        return workerRepository.saveAll(workers).map(workerMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, workerRequest: WorkerRequest): WorkerDto {
        log.trace("worker update -> uuid: $uuid, request: $workerRequest")
        val worker = getById(uuid)
        workerMapper.update(workerRequest, worker)
        return workerMapper.toDto(workerRepository.save(worker))
    }

    @Transactional
    override fun updateMultiple(workerDtoList: List<WorkerDto>): List<WorkerDto> {
        log.trace("worker updateMultiple -> workerDtoList: ${objectMapper.writeValueAsString(workerDtoList)}")
        val workers = workerRepository.findAllById(workerDtoList.mapNotNull { it.uuid })
        workers.forEach { worker ->
            workerMapper.update(workerMapper.toRequest(workerDtoList.first { it.uuid == worker.uuid }), worker)
        }
        return workerRepository.saveAll(workers).map(workerMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("worker delete -> uuid: $uuid")
        val worker = getById(uuid)
        worker.deleted = true
        worker.deletedAt = LocalDateTime.now()
        workerRepository.save(worker)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("worker deleteMultiple -> uuid: $uuidList")
        val workers = workerRepository.findAllById(uuidList)
        workers.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        workerRepository.saveAll(workers)
    }

    fun createSpec(where: String): Specification<Worker> {
        var finalSpec = Specification { root: Root<Worker>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<Worker>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
