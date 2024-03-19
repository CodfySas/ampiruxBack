package com.osia.nota_maestro.repository.classroomResourceTask

import com.osia.nota_maestro.model.ClassroomResourceTask
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("classroomResourceTask.crud_repository")
interface ClassroomResourceTaskRepository :
    JpaRepository<ClassroomResourceTask, UUID>,
    JpaSpecificationExecutor<ClassroomResourceTask>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classroom_resource_tasks", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM classroom_resource_tasks where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ClassroomResourceTask>

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomResourceTask SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidClassroomResource(uuid: UUID): List<ClassroomResourceTask>

    fun findAllByUuidStudentAndUuidClassroomResource(uuid: UUID, uuidTask: UUID): Optional<ClassroomResourceTask>

    fun findAllByUuidClassroomStudentAndUuidClassroomResource(uuid: UUID, uuidTask: UUID): Optional<ClassroomResourceTask>

}
