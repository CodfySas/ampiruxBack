package com.osia.nota_maestro.controller.accompaniment.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentCompleteDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentDto
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentMapper
import com.osia.nota_maestro.dto.accompaniment.v1.AccompanimentRequest
import com.osia.nota_maestro.dto.accompanimentStudent.v1.AccompanimentStudentDto
import com.osia.nota_maestro.dto.log.v1.LogRequest
import com.osia.nota_maestro.service.accompaniment.AccompanimentService
import com.osia.nota_maestro.service.log.LogService
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

@RestController("accompaniment.v1.crud")
@CrossOrigin
@RequestMapping("v1/accompaniments")
@Validated
class AccompanimentController(
    private val accompanimentService: AccompanimentService,
    private val accompanimentMapper: AccompanimentMapper,
    private val logService: LogService
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<AccompanimentDto> {
        return accompanimentService.findAll(pageable, school)
    }

    @GetMapping("/complete")
    fun getComplete(@RequestHeader school: UUID): List<AccompanimentCompleteDto> {
        return accompanimentService.getComplete(school)
    }

    @PostMapping("/complete")
    fun saveComplete(@RequestBody complete: List<AccompanimentCompleteDto>, @RequestHeader school: UUID, @RequestHeader user: UUID?): List<AccompanimentCompleteDto> {
        val time = LocalDateTime.now(ZoneId.of("America/Bogota"))
        val req = LogRequest().apply {
            this.uuidSchool = school
            this.day = LocalDate.now(ZoneId.of("America/Bogota"))
            this.hour = "${String.format("%02d", time.hour)}:${String.format("%02d", time.minute)}:${String.format("%02d", time.second)}"
            this.uuidUser = user
            this.movement = "ha actualizado los docentes acompañantes de cada curso"
        }
        return try {
            val res = accompanimentService.saveComplete(complete, school)
            logService.save(
                req.apply {
                    this.status = "Completado"
                }
            )
            res
        } catch (e: Exception) {
            logService.save(
                req.apply {
                    this.status = "Error"
                    this.detail = "${e.message}"
                }
            )
            emptyList()
        }
    }

    @GetMapping("/my/{user}")
    fun getMyGroups(@PathVariable user: UUID, @RequestHeader school: UUID): List<AccompanimentCompleteDto> {
        return accompanimentService.getMyGroups(user, school)
    }

    @GetMapping("/detail/{classroom}/{period}")
    fun getStudentClassrooms(@PathVariable classroom: UUID, @PathVariable period: Int): List<AccompanimentStudentDto> {
        return accompanimentService.getByClassroom(classroom, period)
    }

    @PostMapping("/detail/{classroom}/{period}")
    fun submit(@PathVariable classroom: UUID, @RequestHeader school: UUID, @PathVariable period: Int, @RequestHeader user: UUID?, @RequestBody req: List<AccompanimentStudentDto>): List<AccompanimentStudentDto> {
        val time = LocalDateTime.now(ZoneId.of("America/Bogota"))
        val req1 = LogRequest().apply {
            this.day = LocalDate.now(ZoneId.of("America/Bogota"))
            this.uuidSchool = school
            this.hour = "${String.format("%02d", time.hour)}:${String.format("%02d", time.minute)}:${String.format("%02d", time.second)}"
            this.uuidUser = user
            this.movement = "ha actualizado la información del acompañamiento de los estudiantes"
        }
        return try {
            val res = accompanimentService.submit(classroom, period, req)
            logService.save(
                req1.apply {
                    this.status = "Completado"
                }
            )
            res
        } catch (e: Exception) {
            logService.save(
                req1.apply {
                    this.status = "Error"
                    this.detail = "${e.message}"
                }
            )
            emptyList()
        }
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<AccompanimentDto> {
        return accompanimentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return accompanimentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<AccompanimentDto> {
        return ResponseEntity.ok().body(accompanimentMapper.toDto(accompanimentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<AccompanimentDto>> {
        return ResponseEntity.ok().body(accompanimentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: AccompanimentRequest
    ): ResponseEntity<AccompanimentDto> {
        return ResponseEntity(accompanimentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody accompanimentRequestList: List<AccompanimentRequest>
    ): ResponseEntity<List<AccompanimentDto>> {
        return ResponseEntity(accompanimentService.saveMultiple(accompanimentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: AccompanimentRequest
    ): ResponseEntity<AccompanimentDto> {
        return ResponseEntity.ok().body(accompanimentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody accompanimentDtoList: List<AccompanimentDto>
    ): ResponseEntity<List<AccompanimentDto>> {
        return ResponseEntity.ok().body(accompanimentService.updateMultiple(accompanimentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        accompanimentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        accompanimentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
