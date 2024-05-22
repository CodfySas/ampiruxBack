package com.osia.nota_maestro.repository.classroomResource

import com.osia.nota_maestro.model.ClassroomResource
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("classroomResource.crud_repository")
interface ClassroomResourceRepository :
    JpaRepository<ClassroomResource, UUID>,
    JpaSpecificationExecutor<ClassroomResource>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM classroom_resources", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM classroom_resources where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ClassroomResource>

    @Modifying
    @Transactional
    @Query("UPDATE ClassroomResource SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByClassroomAndSubject(classroom: UUID, subject: UUID): List<ClassroomResource>
}
