package com.osia.nota_maestro.controller.studentPosition.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionDto
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionMapper
import com.osia.nota_maestro.dto.studentPosition.v1.StudentPositionRequest
import com.osia.nota_maestro.service.studentPosition.StudentPositionService
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

@RestController("studentPosition.v1.crud")
@CrossOrigin
@RequestMapping("v1/student-positions")
@Validated
class StudentPositionController(
    private val studentPositionService: StudentPositionService,
    private val studentPositionMapper: StudentPositionMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<StudentPositionDto> {
        return studentPositionService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<StudentPositionDto> {
        return studentPositionService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return studentPositionService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<StudentPositionDto> {
        return ResponseEntity.ok().body(studentPositionMapper.toDto(studentPositionService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<StudentPositionDto>> {
        return ResponseEntity.ok().body(studentPositionService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: StudentPositionRequest
    ): ResponseEntity<StudentPositionDto> {
        return ResponseEntity(studentPositionService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody studentPositionRequestList: List<StudentPositionRequest>
    ): ResponseEntity<List<StudentPositionDto>> {
        return ResponseEntity(studentPositionService.saveMultiple(studentPositionRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: StudentPositionRequest
    ): ResponseEntity<StudentPositionDto> {
        return ResponseEntity.ok().body(studentPositionService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody studentPositionDtoList: List<StudentPositionDto>
    ): ResponseEntity<List<StudentPositionDto>> {
        return ResponseEntity.ok().body(studentPositionService.updateMultiple(studentPositionDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        studentPositionService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        studentPositionService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
