package com.osia.nota_maestro.controller.classroomResourceTask.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskDto
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskMapper
import com.osia.nota_maestro.dto.classroomResourceTask.v1.ClassroomResourceTaskRequest
import com.osia.nota_maestro.service.classroomResourceTask.ClassroomResourceTaskService
import com.osia.nota_maestro.util.SubmitFile
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController("classroomResourceTask.v1.crud")
@CrossOrigin
@RequestMapping("v1/classroom-resource-tasks")
@Validated
class ClassroomResourceTaskController(
    private val classroomResourceTaskService: ClassroomResourceTaskService,
    private val classroomResourceTaskMapper: ClassroomResourceTaskMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ClassroomResourceTaskDto> {
        return classroomResourceTaskService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(
        pageable: Pageable,
        @PathVariable where: String,
        @RequestHeader school: UUID
    ): Page<ClassroomResourceTaskDto> {
        return classroomResourceTaskService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return classroomResourceTaskService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ClassroomResourceTaskDto> {
        return ResponseEntity.ok().body(classroomResourceTaskMapper.toDto(classroomResourceTaskService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ClassroomResourceTaskDto>> {
        return ResponseEntity.ok().body(classroomResourceTaskService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ClassroomResourceTaskRequest
    ): ResponseEntity<ClassroomResourceTaskDto> {
        return ResponseEntity(classroomResourceTaskService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody classroomResourceTaskRequestList: List<ClassroomResourceTaskRequest>
    ): ResponseEntity<List<ClassroomResourceTaskDto>> {
        return ResponseEntity(
            classroomResourceTaskService.saveMultiple(classroomResourceTaskRequestList),
            HttpStatus.CREATED
        )
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClassroomResourceTaskRequest
    ): ResponseEntity<ClassroomResourceTaskDto> {
        return ResponseEntity.ok().body(classroomResourceTaskService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody classroomResourceTaskDtoList: List<ClassroomResourceTaskDto>
    ): ResponseEntity<List<ClassroomResourceTaskDto>> {
        return ResponseEntity.ok().body(classroomResourceTaskService.updateMultiple(classroomResourceTaskDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        classroomResourceTaskService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        classroomResourceTaskService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get-task-responses/{classroom}/{task}")
    fun getResponses(
        @PathVariable classroom: UUID,
        @PathVariable task: UUID
    ): ResponseEntity<List<ClassroomResourceTaskDto>> {
        return ResponseEntity.ok().body(classroomResourceTaskService.getByClassroomAndTask(classroom, task))
    }

    @PostMapping("/submit-task-responses/{classroom}/{task}")
    fun submitResponses(
        @PathVariable classroom: UUID,
        @PathVariable task: UUID,
        @RequestBody req: List<ClassroomResourceTaskDto>
    ): ResponseEntity<List<ClassroomResourceTaskDto>> {
        return ResponseEntity.ok().body(classroomResourceTaskService.submitClassroomAndTask(classroom, task, req))
    }

    @GetMapping("/my-response-task/{uuid}/{task}")
    fun getMyResponseTask(
        @PathVariable uuid: UUID,
        @PathVariable task: UUID
    ): ResponseEntity<ClassroomResourceTaskDto> {
        return ResponseEntity.ok().body(classroomResourceTaskService.getMyClassroomResourceTask(uuid, task))
    }

    @PostMapping("/submit-response/{task}/{uuid}")
    fun submitTaskResponse(
        @PathVariable task: UUID,
        @PathVariable uuid: UUID,
        @RequestParam("file") file: MultipartFile?,
        @RequestParam("description") description: String?
    ): ResponseEntity<ClassroomResourceTaskDto> {
        val originalFilename = file?.originalFilename
        val extension = originalFilename?.substringAfterLast(".")
        var hasFile = false
        if (file != null) {
            SubmitFile().reviewExt(extension ?: "")
            hasFile = true
        }
        val submit = classroomResourceTaskService.submitMyClassroomResourceTask(
            uuid,
            task, file?.originalFilename ?: "",
            description ?: "",
            hasFile,
            extension ?: ""
        )
        if (file != null) {
            SubmitFile().submitFile(submit.uuid!!, extension, file)
        }
        return ResponseEntity.ok(submit)
    }

    @GetMapping("/download/{uuid}")
    fun download(@PathVariable uuid: UUID): ResponseEntity<ByteArray> {
        return classroomResourceTaskService.download(uuid)
    }
}
