package com.osia.nota_maestro.controller.worker.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.worker.v1.WorkerDto
import com.osia.nota_maestro.dto.worker.v1.WorkerMapper
import com.osia.nota_maestro.dto.worker.v1.WorkerRequest
import com.osia.nota_maestro.service.worker.WorkerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("worker.v1.crud")
@CrossOrigin
@RequestMapping("v1/workers")
@Validated
class WorkerController(
    private val workerService: WorkerService,
    private val workerMapper: WorkerMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable): Page<WorkerDto> {
        return workerService.findAll(pageable)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<WorkerDto> {
        return workerService.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return workerService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<WorkerDto> {
        return ResponseEntity.ok().body(workerMapper.toDto(workerService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<WorkerDto>> {
        return ResponseEntity.ok().body(workerService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: WorkerRequest
    ): ResponseEntity<WorkerDto> {
        return ResponseEntity(workerService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody workerRequestList: List<WorkerRequest>
    ): ResponseEntity<List<WorkerDto>> {
        return ResponseEntity(workerService.saveMultiple(workerRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: WorkerRequest
    ): ResponseEntity<WorkerDto> {
        return ResponseEntity.ok().body(workerService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody workerDtoList: List<WorkerDto>
    ): ResponseEntity<List<WorkerDto>> {
        return ResponseEntity.ok().body(workerService.updateMultiple(workerDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        workerService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        workerService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
