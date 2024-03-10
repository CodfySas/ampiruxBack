package com.osia.nota_maestro.service.mesh.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.mesh.v1.MeshDto
import com.osia.nota_maestro.dto.mesh.v1.MeshMapper
import com.osia.nota_maestro.dto.mesh.v1.MeshRequest
import com.osia.nota_maestro.model.Mesh
import com.osia.nota_maestro.repository.classroom.ClassroomRepository
import com.osia.nota_maestro.repository.classroomStudent.ClassroomStudentRepository
import com.osia.nota_maestro.repository.mesh.MeshRepository
import com.osia.nota_maestro.repository.school.SchoolRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.mesh.MeshService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID

@Service("mesh.crud_service")
@Transactional
class MeshServiceImpl(
    private val meshRepository: MeshRepository,
    private val meshMapper: MeshMapper,
    private val objectMapper: ObjectMapper,
    private val userRepository: UserRepository,
    private val classroomRepository: ClassroomRepository,
    private val classroomStudentRepository: ClassroomStudentRepository,
    private val schoolRepository: SchoolRepository
) : MeshService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("mesh count -> increment: $increment")
        return meshRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Mesh {
        return meshRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Mesh $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<MeshDto> {
        log.trace("mesh findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return meshRepository.findAllById(uuidList).map(meshMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<MeshDto> {
        log.trace("mesh findAll -> pageable: $pageable")
        return meshRepository.findAll(Specification.where(CreateSpec<Mesh>().createSpec("", school)), pageable).map(meshMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<MeshDto> {
        log.trace("mesh findAllByFilter -> pageable: $pageable, where: $where")
        return meshRepository.findAll(Specification.where(CreateSpec<Mesh>().createSpec(where, null)), pageable).map(meshMapper::toDto)
    }

    @Transactional
    override fun save(meshRequest: MeshRequest, replace: Boolean): MeshDto {
        log.trace("mesh save -> request: $meshRequest")
        val savedMesh = meshMapper.toModel(meshRequest)
        return meshMapper.toDto(meshRepository.save(savedMesh))
    }

    @Transactional
    override fun saveMultiple(meshRequestList: List<MeshRequest>): List<MeshDto> {
        log.trace("mesh saveMultiple -> requestList: ${objectMapper.writeValueAsString(meshRequestList)}")
        val meshs = meshRequestList.map(meshMapper::toModel)
        return meshRepository.saveAll(meshs).map(meshMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, meshRequest: MeshRequest, includeDelete: Boolean): MeshDto {
        log.trace("mesh update -> uuid: $uuid, request: $meshRequest")
        val mesh = if (!includeDelete) {
            getById(uuid)
        } else {
            meshRepository.getByUuid(uuid).get()
        }
        meshMapper.update(meshRequest, mesh)
        return meshMapper.toDto(meshRepository.save(mesh))
    }

    @Transactional
    override fun updateMultiple(meshDtoList: List<MeshDto>, classroom: UUID, subject: UUID, period: Int): List<MeshDto> {
        log.trace("mesh updateMultiple -> meshDtoList: ${objectMapper.writeValueAsString(meshDtoList)}")
        val getBy = getBy(classroom, subject, period)
        val toSave = meshDtoList.filter { it.uuid == null }
        val toUpdate = meshDtoList.filter { it.uuid != null }
        val toDelete = getBy.filterNot { m-> meshDtoList.mapNotNull { it.uuid }.contains(m.uuid) }.mapNotNull { it.uuid }
        val toUpdateAll = mutableListOf<Mesh>()
        toUpdate.forEach {
            val foundSaved = getBy.first { f-> f.uuid == it.uuid }
            meshMapper.update(meshMapper.toRequest(it), foundSaved)
            toUpdateAll.add(foundSaved)
        }
        deleteMultiple(toDelete)
        meshRepository.saveAll(toUpdateAll)
        return saveMultiple(toSave.map(meshMapper::toRequest))
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("mesh delete -> uuid: $uuid")
        val mesh = getById(uuid)
        mesh.deleted = true
        mesh.deletedAt = LocalDateTime.now()
        meshRepository.save(mesh)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("mesh deleteMultiple -> uuid: $uuidList")
        val meshs = meshRepository.findAllById(uuidList)
        meshs.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        meshRepository.saveAll(meshs)
    }

    override fun getBy(classroom: UUID, subject: UUID, period: Int): List<Mesh> {
       return meshRepository.findAllByClassroomAndSubjectAndPeriod(classroom, subject, period)
    }

    override fun getByMy(uuid: UUID, subject: UUID, period: Int): List<MeshDto> {
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
        return getBy(classroomStudent.uuidClassroom!!, subject, period).map(meshMapper::toDto).sortedBy { it.position }
    }
}
