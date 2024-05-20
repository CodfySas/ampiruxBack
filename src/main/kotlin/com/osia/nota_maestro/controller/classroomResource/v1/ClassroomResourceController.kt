package com.osia.nota_maestro.controller.classroomResource.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceDto
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceMapper
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceRequest
import com.osia.nota_maestro.dto.classroomResource.v1.ExamCompleteDto
import com.osia.nota_maestro.dto.examQuestion.v1.ExamQuestionDto
import com.osia.nota_maestro.service.classroomResource.ClassroomResourceService
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale
import java.util.UUID

@RestController("classroomResource.v1.crud")
@CrossOrigin
@RequestMapping("v1/classroom-resources")
@Validated
class ClassroomResourceController(
    private val classroomResourceService: ClassroomResourceService,
    private val classroomResourceMapper: ClassroomResourceMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<ClassroomResourceDto> {
        return classroomResourceService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(
        pageable: Pageable,
        @PathVariable where: String,
        @RequestHeader school: UUID
    ): Page<ClassroomResourceDto> {
        return classroomResourceService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return classroomResourceService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<ClassroomResourceDto> {
        return ResponseEntity.ok().body(classroomResourceMapper.toDto(classroomResourceService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<ClassroomResourceDto>> {
        return ResponseEntity.ok().body(classroomResourceService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: ClassroomResourceRequest
    ): ResponseEntity<ClassroomResourceDto> {
        return ResponseEntity(classroomResourceService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody classroomResourceRequestList: List<ClassroomResourceRequest>
    ): ResponseEntity<List<ClassroomResourceDto>> {
        return ResponseEntity(classroomResourceService.saveMultiple(classroomResourceRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: ClassroomResourceRequest
    ): ResponseEntity<ClassroomResourceDto> {
        return ResponseEntity.ok().body(classroomResourceService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody classroomResourceDtoList: List<ClassroomResourceDto>
    ): ResponseEntity<List<ClassroomResourceDto>> {
        return ResponseEntity.ok().body(classroomResourceService.updateMultiple(classroomResourceDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        classroomResourceService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        classroomResourceService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get/{classroom}/{subject}")
    fun getBy(@PathVariable classroom: UUID, @PathVariable subject: UUID): List<List<ClassroomResourceDto>> {
        return classroomResourceService.getBy(classroom, subject)
    }

    @GetMapping("/get-my/{uuid}/{subject}")
    fun getByMy(@PathVariable uuid: UUID, @PathVariable subject: UUID): List<List<ClassroomResourceDto>> {
        return classroomResourceService.getByMy(uuid, subject)
    }

    @GetMapping("/download/{uuid}")
    fun download(@PathVariable uuid: UUID): ResponseEntity<ByteArray> {
        return classroomResourceService.download(uuid)
    }

    @PostMapping("/submit/{name}/{classroom}/{subject}/{period}")
    fun submitResource(
        @RequestParam("file") file: MultipartFile,
        @PathVariable name: String,
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable period: Int
    ): ResponseEntity<ByteArray> {
        val originalFilename = file.originalFilename
        val extension = originalFilename?.substringAfterLast(".")
        SubmitFile().reviewExt(extension ?: "")
        val newResource = classroomResourceService.save(ClassroomResourceRequest().apply {
            this.classroom = classroom
            this.subject = subject
            this.period = period
            this.name = name
            this.type = extension
            this.ext = extension
        })
        return SubmitFile().submitFile(newResource.uuid!!, extension, file)
    }

    @PostMapping("/submit-task/{classroom}/{subject}/{period}")
    fun submitTask(
        @RequestParam("file") file: MultipartFile?,
        @RequestParam("name") name: String,
        @RequestParam("description") description: String,
        @RequestParam("last_day") lastDay: String,
        @RequestParam("last_hour") lastHour: String,
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable period: Int
    ): ResponseEntity<ClassroomResourceDto> {
        val originalFilename = file?.originalFilename
        val extension = originalFilename?.substringAfterLast(".")

        val datePart = lastDay.substring(0, 15)
        val dateFormatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH)
        val date = dateFormatter.parse(datePart)
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        if (file != null) {
            SubmitFile().reviewExt(extension ?: "")
        }

        val newTask = classroomResourceService.save(ClassroomResourceRequest().apply {
            this.type = "task"
            this.description = description
            this.finishTime = localDate
            this.lastHour = lastHour
            if (file != null) {
                this.hasFile = true
            }
            this.classroom = classroom
            this.subject = subject
            this.period = period
            this.name = name
            this.ext = extension
        })

        if (file != null) {
            SubmitFile().submitFile(newTask.uuid!!, extension, file)
        }

        return ResponseEntity.ok(newTask)
    }

    @PatchMapping("/update-task/{uuid}")
    fun updateTask(
        @RequestParam("file") file: MultipartFile?,
        @RequestParam("name") name: String,
        @RequestParam("description") description: String,
        @RequestParam("last_day") lastDay: String,
        @RequestParam("last_hour") lastHour: String,
        @PathVariable uuid: UUID
    ): ResponseEntity<ClassroomResourceDto> {
        val originalFilename = file?.originalFilename
        val extension = originalFilename?.substringAfterLast(".")

        val datePart = lastDay.substring(0, 15)
        val dateFormatter = SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH)
        val date = dateFormatter.parse(datePart)
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        var hasFile: Boolean? = null
        var extt: String? = null
        if (file != null) {
            SubmitFile().reviewExt(extension ?: "")
            hasFile = true
            extt = extension
        }

        val updated = classroomResourceService.update(uuid, ClassroomResourceRequest().apply {
            this.hasFile = hasFile
            this.name = name
            this.description = description
            this.finishTime = localDate
            this.lastHour = lastHour
            this.ext = extt
        })

        if (file != null) {
            SubmitFile().submitFile(updated.uuid!!, extension, file)
        }

        return ResponseEntity.ok(updated)
    }

    @GetMapping("/get-exam/{uuid}/{exam}")
    fun getCompleteExamByTeacher(@PathVariable uuid: UUID, @PathVariable exam: UUID): ResponseEntity<ExamCompleteDto> {
        return ResponseEntity.ok(classroomResourceService.getCompleteExamByTeacher(uuid, exam, true))
    }

    @GetMapping("/get-exam-student/{uuid}/{exam}")
    fun getExamByStudent(@PathVariable uuid: UUID, @PathVariable exam: UUID): ResponseEntity<ExamCompleteDto> {
        return ResponseEntity.ok(classroomResourceService.getCompleteExamByTeacher(uuid, exam, false))
    }

    @GetMapping("/can-do/{uuid}/{exam}")
    fun canDoExam(@PathVariable uuid: UUID, @PathVariable exam: UUID): ResponseEntity<Boolean> {
        var res = false
        val getExam = classroomResourceService.getById(exam)
        val now = LocalDateTime.now()

        val iTime = (getExam.initHour ?: "00:00").split(":")
        val fTime = (getExam.lastHour ?: "00:00").split(":")
        val iExamDate = LocalDateTime.of(getExam.initTime, LocalTime.MIDNIGHT).plusHours(iTime[0].toLong())
            .plusMinutes(iTime[1].toLong())
        val fExamDate = LocalDateTime.of(getExam.finishTime, LocalTime.MIDNIGHT).plusHours(fTime[0].toLong())
            .plusMinutes(fTime[1].toLong())

        if (now in iExamDate..fExamDate) {
            res = true
        }
        return ResponseEntity.ok(res)
    }

    @PostMapping("/submit-exam/{classroom}/{subject}/{period}")
    fun submitExam(
        @PathVariable classroom: UUID,
        @PathVariable subject: UUID,
        @PathVariable period: Int,
        @RequestBody req: ExamCompleteDto
    ): ResponseEntity<ExamCompleteDto> {
        return ResponseEntity.ok(classroomResourceService.submitExam(req, classroom, subject, period))
    }

    @PostMapping("/submit-attempt/{uuid}/{exam}")
    fun submitAttempt(
        @PathVariable uuid: UUID,
        @PathVariable exam: UUID,
        @RequestBody responses: List<ExamQuestionDto>
    ): ResponseEntity<List<ExamQuestionDto>> {
        return ResponseEntity.ok(classroomResourceService.submitAttempt(uuid, exam, responses))
    }
}
