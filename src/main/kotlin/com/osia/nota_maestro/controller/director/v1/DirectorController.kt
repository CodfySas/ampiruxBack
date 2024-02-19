package com.osia.nota_maestro.controller.director.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.director.v1.DirectorCompleteDto
import com.osia.nota_maestro.dto.director.v1.DirectorDto
import com.osia.nota_maestro.dto.director.v1.DirectorMapper
import com.osia.nota_maestro.dto.director.v1.DirectorRequest
import com.osia.nota_maestro.dto.directorStudent.v1.DirectorStudentDto
import com.osia.nota_maestro.service.director.DirectorService
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

@RestController("director.v1.crud")
@CrossOrigin
@RequestMapping("v1/directors")
@Validated
class DirectorController(
    private val directorService: DirectorService,
    private val directorMapper: DirectorMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<DirectorDto> {
        return directorService.findAll(pageable, school)
    }

    @GetMapping("/complete")
    fun getComplete(@RequestHeader school: UUID): List<DirectorCompleteDto> {
        return directorService.getComplete(school)
    }

    @PostMapping("/complete")
    fun saveComplete(@RequestBody complete: List<DirectorCompleteDto>): List<DirectorCompleteDto> {
        return directorService.saveComplete(complete)
    }

    @GetMapping("/my/{user}")
    fun getMyGroups(@PathVariable user: UUID, @RequestHeader school: UUID): List<DirectorCompleteDto>{
        return directorService.getMyGroups(user, school)
    }

    @GetMapping("/detail/{classroom}/{period}")
    fun getStudentClassrooms(@PathVariable classroom: UUID, @PathVariable period: Int): List<DirectorStudentDto>{
        return directorService.getByClassroom(classroom, period)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<DirectorDto> {
        return directorService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return directorService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<DirectorDto> {
        return ResponseEntity.ok().body(directorMapper.toDto(directorService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<DirectorDto>> {
        return ResponseEntity.ok().body(directorService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: DirectorRequest
    ): ResponseEntity<DirectorDto> {
        return ResponseEntity(directorService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody directorRequestList: List<DirectorRequest>
    ): ResponseEntity<List<DirectorDto>> {
        return ResponseEntity(directorService.saveMultiple(directorRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: DirectorRequest
    ): ResponseEntity<DirectorDto> {
        return ResponseEntity.ok().body(directorService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody directorDtoList: List<DirectorDto>
    ): ResponseEntity<List<DirectorDto>> {
        return ResponseEntity.ok().body(directorService.updateMultiple(directorDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        directorService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        directorService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
