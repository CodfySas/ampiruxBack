package com.osia.nota_maestro.controller.planning.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.dto.planning.v1.PlanningCompleteRequest
import com.osia.nota_maestro.dto.planning.v1.PlanningDto
import com.osia.nota_maestro.dto.planning.v1.PlanningMapper
import com.osia.nota_maestro.dto.planning.v1.PlanningRequest
import com.osia.nota_maestro.repository.planning.PlanningRepository
import com.osia.nota_maestro.service.log.LogService
import com.osia.nota_maestro.service.planning.PlanningService
import com.osia.nota_maestro.util.SubmitFile
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@RestController("planning.v1.crud")
@CrossOrigin
@RequestMapping("v1/plannings")
@Validated
class PlanningController(
    private val planningService: PlanningService,
    private val planningMapper: PlanningMapper,
    private val planningRepository: PlanningRepository,
    private val logService: LogService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/submit")
    fun submitPlanning(
        @RequestBody p: PlanningCompleteRequest,
        @RequestHeader school: UUID,
        @RequestHeader user: UUID?
    ): ResponseEntity<PlanningDto> {
        val founded = planningRepository.findFirstByClassroomAndSubjectAndWeek(p.classroom!!, p.subject!!, p.week!!)
        val newResource = if (founded.isPresent) {
            planningMapper.toDto(founded.get())
            planningService.update(
                founded.get().uuid!!,
                PlanningRequest().apply {
                    this.userReview = p.my
                    this.status = "pending"
                    this.area = p.area
                }
            )
        } else {
            planningService.save(
                PlanningRequest().apply {
                    this.classroom = p.classroom
                    this.subject = p.subject
                    this.week = p.week
                    this.userReview = p.my
                    this.status = "pending"
                    this.area = p.area
                },
                school
            )
        }
        val time = LocalDateTime.now(ZoneId.of("America/Bogota"))
        logService.save(
            LogRequest().apply {
                this.day = LocalDate.now(ZoneId.of("America/Bogota"))
                this.uuidSchool = school
                this.hour = "${String.format("%02d", time.hour)}:${String.format("%02d", time.minute)}:${String.format("%02d", time.second)}"
                this.uuidUser = user
                this.movement = "ha actualizado la planeación"
                this.status = "Completado"
            }
        )
        return ResponseEntity.ok(newResource)
    }

    @PostMapping("/submit-my")
    fun submitPlanningByTeacher(
        @RequestBody p: PlanningCompleteRequest,
        @RequestHeader school: UUID,
        @RequestHeader user: UUID?
    ): ResponseEntity<PlanningDto> {
        val founded = planningRepository.findFirstByClassroomAndUuidTeacherAndWeek(p.classroom!!, p.teacher!!, p.week!!)
        val newResource = if (founded.isPresent) {
            planningMapper.toDto(founded.get())
            planningService.update(
                founded.get().uuid!!,
                PlanningRequest().apply {
                    this.userReview = p.teacher
                    this.status = "pending"
                    this.area = p.area
                }
            )
        } else {
            planningService.save(
                PlanningRequest().apply {
                    this.classroom = p.classroom
                    this.uuidTeacher = p.teacher
                    this.week = p.week
                    this.userReview = p.teacher
                    this.status = "pending"
                    this.area = p.area
                },
                school
            )
        }
        val time = LocalDateTime.now(ZoneId.of("America/Bogota"))
        logService.save(
            LogRequest().apply {
                this.day = LocalDate.now(ZoneId.of("America/Bogota"))
                this.uuidSchool = school
                this.hour = "${String.format("%02d", time.hour)}:${String.format("%02d", time.minute)}:${String.format("%02d", time.second)}"
                this.uuidUser = user
                this.movement = "ha actualizado la planeación"
                this.status = "Completado"
            }
        )
        return ResponseEntity.ok(newResource)
    }

    @GetMapping("/download/{uuid}/{ext}")
    fun downloadPlanning(@PathVariable uuid: UUID, @PathVariable ext: String): ResponseEntity<ByteArray> {
        return try {
            val targetLocation: Path = Path.of("src/main/resources/plannings/$uuid.$ext")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(SubmitFile().determineMediaType(ext)).body(imageBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }

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
        @Validated(OnCreate::class) @RequestBody request: PlanningRequest,
        @RequestHeader school: UUID
    ): ResponseEntity<PlanningDto> {
        return ResponseEntity(planningService.save(request, school), HttpStatus.CREATED)
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
    fun getBy(@PathVariable classroom: UUID, @PathVariable subject: UUID, @PathVariable week: Int): PlanningDto {
        return planningMapper.toDto(planningService.getBy(classroom, subject, week))
    }

    @GetMapping("/get-my/{classroom}/{my}/{week}")
    fun getByGroup(@PathVariable classroom: UUID, @PathVariable my: UUID, @PathVariable week: Int): PlanningDto {
        return planningMapper.toDto(planningService.getByTeacher(classroom, my, week))
    }
}
