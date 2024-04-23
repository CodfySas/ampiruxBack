package com.osia.nota_maestro.repository.planning

import com.osia.nota_maestro.model.Planning
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("planning.crud_repository")
interface PlanningRepository :
    JpaRepository<Planning, UUID>,
    JpaSpecificationExecutor<Planning>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM plannings", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM plannings where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Planning>

    @Modifying
    @Transactional
    @Query("UPDATE Planning SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findFirstByClassroomAndSubjectAndWeek(classroom: UUID, subject: UUID, week: Int): Optional<Planning>

    fun findAllByClassroomAndSubjectAndWeek(classroom: UUID, subject: UUID, week: Int): List<Planning>
}
