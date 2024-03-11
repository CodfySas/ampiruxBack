package com.osia.nota_maestro.service.classroomResource.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceDto
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceMapper
import com.osia.nota_maestro.dto.classroomResource.v1.ClassroomResourceRequest
import com.osia.nota_maestro.model.ClassroomResource
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomResource.ClassroomResourceRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.schoolPeriod.SchoolPeriodRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.classroomResource.ClassroomResourceService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.UUID

@Service("classroomResource.crud_service")
@Transactional
class ClassroomResourceServiceImpl(
    private val classroomResourceRepository: ClassroomResourceRepository,
    private val classroomResourceMapper: ClassroomResourceMapper,
    private val objectMapper: ObjectMapper,
    private val classroomRepository: ClassroomRepository,
    private val schoolRepository: SchoolRepository,
    private val schoolPeriodRepository: SchoolPeriodRepository,
    private val userRepository: UserRepository,
    private val classroomStudentRepository: ClassroomStudentRepository
) : ClassroomResourceService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("classroomResource count -> increment: $increment")
        return classroomResourceRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): ClassroomResource {
        return classroomResourceRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "ClassroomResource $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<ClassroomResourceDto> {
        log.trace("classroomResource findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return classroomResourceRepository.findAllById(uuidList).map(classroomResourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<ClassroomResourceDto> {
        log.trace("classroomResource findAll -> pageable: $pageable")
        return classroomResourceRepository.findAll(Specification.where(CreateSpec<ClassroomResource>().createSpec("", school)), pageable).map(classroomResourceMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<ClassroomResourceDto> {
        log.trace("classroomResource findAllByFilter -> pageable: $pageable, where: $where")
        return classroomResourceRepository.findAll(Specification.where(CreateSpec<ClassroomResource>().createSpec(where, school)), pageable).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun save(classroomResourceRequest: ClassroomResourceRequest, replace: Boolean): ClassroomResourceDto {
        log.trace("classroomResource save -> request: $classroomResourceRequest")
        val savedClassroomResource = classroomResourceMapper.toModel(classroomResourceRequest)
        return classroomResourceMapper.toDto(classroomResourceRepository.save(savedClassroomResource))
    }

    @Transactional
    override fun saveMultiple(classroomResourceRequestList: List<ClassroomResourceRequest>): List<ClassroomResourceDto> {
        log.trace("classroomResource saveMultiple -> requestList: ${objectMapper.writeValueAsString(classroomResourceRequestList)}")
        val classroomResources = classroomResourceRequestList.map(classroomResourceMapper::toModel)
        return classroomResourceRepository.saveAll(classroomResources).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, classroomResourceRequest: ClassroomResourceRequest, includeDelete: Boolean): ClassroomResourceDto {
        log.trace("classroomResource update -> uuid: $uuid, request: $classroomResourceRequest")
        val classroomResource = if (!includeDelete) {
            getById(uuid)
        } else {
            classroomResourceRepository.getByUuid(uuid).get()
        }
        classroomResourceMapper.update(classroomResourceRequest, classroomResource)
        return classroomResourceMapper.toDto(classroomResourceRepository.save(classroomResource))
    }

    @Transactional
    override fun updateMultiple(classroomResourceDtoList: List<ClassroomResourceDto>): List<ClassroomResourceDto> {
        log.trace("classroomResource updateMultiple -> classroomResourceDtoList: ${objectMapper.writeValueAsString(classroomResourceDtoList)}")
        val classroomResources = classroomResourceRepository.findAllById(classroomResourceDtoList.mapNotNull { it.uuid })
        classroomResources.forEach { classroomResource ->
            classroomResourceMapper.update(classroomResourceMapper.toRequest(classroomResourceDtoList.first { it.uuid == classroomResource.uuid }), classroomResource)
        }
        return classroomResourceRepository.saveAll(classroomResources).map(classroomResourceMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("classroomResource delete -> uuid: $uuid")
        val classroomResource = getById(uuid)
        classroomResource.deleted = true
        classroomResource.deletedAt = LocalDateTime.now()
        classroomResourceRepository.save(classroomResource)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("classroomResource deleteMultiple -> uuid: $uuidList")
        val classroomResources = classroomResourceRepository.findAllById(uuidList)
        classroomResources.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        classroomResourceRepository.saveAll(classroomResources)
    }

    override fun getBy(classroom: UUID, subject: UUID): List<List<ClassroomResourceDto>> {
        val classroomF = classroomRepository.getByUuid(classroom).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val schoolPeriods = schoolPeriodRepository.findAllByUuidSchool(classroomF.uuidSchool!!)
        val resources = classroomResourceRepository.findAllByClassroomAndSubject(classroom, subject).sortedBy { it.createdAt }
        val finalList = mutableListOf<List<ClassroomResourceDto>>()
        schoolPeriods.forEach {
            val rsP = resources.filter { rs-> rs.period == it.number }
            finalList.add(rsP.map(classroomResourceMapper::toDto))
        }
        return finalList
    }

    override fun getByMy(uuid: UUID, subject: UUID): List<List<ClassroomResourceDto>> {
        val user = userRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val school = schoolRepository.findById(user.uuidSchool!!).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        val classrooms = classroomRepository.findAllByUuidSchoolAndYear(user.uuidSchool!!, school.actualYear!!)
        val classroomStudent = classroomStudentRepository.findFirstByUuidClassroomInAndUuidStudent(classrooms.mapNotNull { it.uuid }.distinct(), uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY)
        }
        return getBy(classroomStudent.uuidClassroom!!, subject)
    }

    override fun download(uuid: UUID): ResponseEntity<ByteArray> {
        val resource = getById(uuid)
        return try {
            val targetLocation: Path = Path.of("src/main/resources/files/${uuid}.${resource.ext}")
            val fileBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(determineMediaType(resource.ext ?: "")).body(fileBytes)
        } catch (ex: Exception) {
            val targetLocation: Path = Path.of("src/main/resources/logos/none.png")
            val imageBytes = Files.readAllBytes(targetLocation)
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes)
        }
    }

    override fun determineMediaType(ext: String): MediaType {
        return when {
            ext.endsWith("png") -> MediaType.IMAGE_PNG
            ext.endsWith("jpg") || ext.endsWith("jpeg") -> MediaType.IMAGE_JPEG
            ext.endsWith("gif") -> MediaType.IMAGE_GIF
            ext.endsWith("pdf") -> MediaType.APPLICATION_PDF
            ext.endsWith("doc") || ext.endsWith("docx") -> MediaType.valueOf("application/msword")
            ext.endsWith("xls") || ext.endsWith("xlsx") -> MediaType.valueOf("application/vnd.ms-excel")
            ext.endsWith("ppt") || ext.endsWith("pptx") -> MediaType.valueOf("application/vnd.ms-powerpoint")
            else -> MediaType.APPLICATION_OCTET_STREAM // Tipo genérico para otros tipos de archivo
        }
    }
}
