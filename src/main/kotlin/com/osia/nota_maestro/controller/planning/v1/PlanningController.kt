package com.osia.nota_maestro.controller.planning.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.planning.v1.PlanningDto
import com.osia.nota_maestro.dto.planning.v1.PlanningMapper
import com.osia.nota_maestro.dto.planning.v1.PlanningRequest
import com.osia.nota_maestro.service.planning.PlanningService
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
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("planning.v1.crud")
@CrossOrigin
@RequestMapping("v1/plannings")
@Validated
class PlanningController(
    private val planningService: PlanningService,
    private val planningMapper: PlanningMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<PlanningDto> {
        return planningService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String): Page<PlanningDto> {
        return planningService.findAllByFilter(pageable, where)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return planningService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<PlanningDto> {
        return ResponseEntity.ok().body(planningMapper.toDto(planningService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<PlanningDto>> {
        return ResponseEntity.ok().body(planningService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: PlanningRequest
    ): ResponseEntity<PlanningDto> {
        return ResponseEntity(planningService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody planningRequestList: List<PlanningRequest>
    ): ResponseEntity<List<PlanningDto>> {
        return ResponseEntity(planningService.saveMultiple(planningRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PlanningRequest
    ): ResponseEntity<PlanningDto> {
        return ResponseEntity.ok().body(planningService.update(uuid, request))
    }

    @PatchMapping("/multiple/{classroom}/{subject}/{period}")
    fun updateMultiple(
        @RequestBody planningDtoList: List<PlanningDto>, @PathVariable classroom: UUID, @PathVariable subject: UUID, @PathVariable period: Int
    ): ResponseEntity<List<PlanningDto>> {
        return ResponseEntity.ok().body(planningService.updateMultiple(planningDtoList,classroom, subject, period))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        planningService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        planningService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get/{classroom}/{subject}/{week}")
    fun getBy(@PathVariable classroom: UUID, @PathVariable subject: UUID, @PathVariable week: Int): List<PlanningDto> {
        val planning = planningService.getBy(classroom,subject,week).map(planningMapper::toDto).sortedBy { it.position }
        return planning.ifEmpty {
            getEmpty()
        }
    }

    @GetMapping("/get-my/{uuid}/{subject}/{week}")
    fun getByMy(@PathVariable uuid: UUID, @PathVariable subject: UUID, @PathVariable week: Int): List<PlanningDto> {
        val planning = planningService.getByMy(uuid, subject, week)
        return planning.ifEmpty {
            getEmpty()
        }
    }

    private fun getEmpty(): List<PlanningDto>{
        val planningList = mutableListOf<PlanningDto>()
        val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        for ((position, day) in daysOfWeek.withIndex()) {
            planningList.add(PlanningDto().apply { this.day = day; this.position = position })
        }
        return planningList
    }
}
