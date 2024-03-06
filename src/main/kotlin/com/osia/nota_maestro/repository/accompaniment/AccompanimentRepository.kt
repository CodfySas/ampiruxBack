package com.osia.nota_maestro.repository.accompaniment

import com.osia.nota_maestro.model.Accompaniment
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("accompaniment.crud_repository")
interface AccompanimentRepository :
    JpaRepository<Accompaniment, UUID>,
    JpaSpecificationExecutor<Accompaniment>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM accompaniments", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM accompaniments where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Accompaniment>

    @Modifying
    @Transactional
    @Query("UPDATE Accompaniment SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomIn(classrooms: List<UUID>): List<Accompaniment>

    fun getAllByUuidTeacher(uuid: UUID): List<Accompaniment>

    fun findFirstByUuidClassroom(uuid: UUID): Optional<Accompaniment>
}
