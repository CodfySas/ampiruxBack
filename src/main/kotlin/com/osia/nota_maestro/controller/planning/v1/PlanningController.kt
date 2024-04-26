package com.osia.nota_maestro.controller.planning.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceRequest
import com.osia.nota_maestro.dto.planning.v1.PlanningDto
import com.osia.nota_maestro.dto.planning.v1.PlanningMapper
import com.osia.nota_maestro.dto.planning.v1.PlanningRequest
import com.osia.nota_maestro.repository.planning.PlanningRepository
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

@RestController("planning.v1.crud")
@CrossOrigin
@RequestMapping("v1/plannings")
@Validated
class PlanningController(
    private val planningService: PlanningService,
    private val planningMapper: PlanningMapper,
    private val planningRepository: PlanningRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("/submit/{classroom}/{subject}/{week}/{my}")
    fun submitPlanning(
        @RequestParam("file") file: MultipartFile,
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable week: Int,
        @PathVariable my: UUID,
        @RequestHeader school: UUID
    ): ResponseEntity<PlanningDto> {
        val originalFilename = file.originalFilename
        val extension = originalFilename?.substringAfterLast(".")
        SubmitFile().reviewExt(extension ?: "")
        val founded = planningRepository.findFirstByClassroomAndSubjectAndWeek(classroom, subject, week)
        var newResource: PlanningDto = PlanningDto()
        if(founded.isPresent){
            newResource = planningMapper.toDto(founded.get())
            planningService.update(newResource.uuid!!, PlanningRequest().apply {
                this.userReview = my
                this.area = file.originalFilename?.substringBeforeLast(".")
                this.topic = file.originalFilename?.substringAfterLast(".")
                this.status = "pending"
            })
        }else{
            newResource = planningService.save(PlanningRequest().apply {
                this.classroom = classroom
                this.subject = subject
                this.week = week
                this.userReview = my
                this.area = file.originalFilename?.substringBeforeLast(".")
                this.topic = file.originalFilename?.substringAfterLast(".")
                this.status = "pending"
            }, school)
        }
        val name = "${newResource.uuid!!}"
        log.info("llega aqui")
        SubmitFile().submitPlanning(name, extension, file)
        return ResponseEntity.ok(newResource)
    }

    @PostMapping("/submit-my/{classroom}/{week}/{my}")
    fun submitPlanningByTeacher(
        @RequestParam("file") file: MultipartFile,
        @PathVariable classroom: UUID,
        @PathVariable week: Int,
        @PathVariable my: UUID,
        @RequestHeader school: UUID
    ): ResponseEntity<PlanningDto> {
        val originalFilename = file.originalFilename
        val extension = originalFilename?.substringAfterLast(".")
        SubmitFile().reviewExt(extension ?: "")
        val founded = planningRepository.findFirstByClassroomAndSubjectAndWeek(classroom, my, week)
        var newResource: PlanningDto = PlanningDto()
        if(founded.isPresent){
            newResource = planningMapper.toDto(founded.get())
            planningService.update(newResource.uuid!!, PlanningRequest().apply {
                this.userReview = my
                this.area = file.originalFilename?.substringBeforeLast(".")
                this.topic = file.originalFilename?.substringAfterLast(".")
                this.status = "pending"
            })
        }else{
            newResource = planningService.save(PlanningRequest().apply {
                this.classroom = classroom
                this.uuidTeacher = my
                this.week = week
                this.userReview = my
                this.area = file.originalFilename?.substringBeforeLast(".")
                this.topic = file.originalFilename?.substringAfterLast(".")
                this.status = "pending"
            }, school)
        }
        val name = "${newResource.uuid!!}"
        SubmitFile().submitPlanning(name, extension, file)
        return ResponseEntity.ok(newResource)
    }

    @GetMapping("/download/{uuid}/{ext}")
    fun downloadPlanning(@PathVariable uuid: UUID, @PathVariable ext: String): ResponseEntity<ByteArray> {
        return try {
            val targetLocation: Path = Path.of("src/main/resources/plannings/${uuid}.${ext}")
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
        return planningMapper.toDto(planningService.getBy(classroom,subject,week))
    }

    @GetMapping("/get-my/{classroom}/{my}/{week}")
    fun getByGroup(@PathVariable classroom: UUID, @PathVariable my: UUID, @PathVariable week: Int): PlanningDto {
        return planningMapper.toDto(planningService.getByTeacher(classroom,my,week))
    }
}
