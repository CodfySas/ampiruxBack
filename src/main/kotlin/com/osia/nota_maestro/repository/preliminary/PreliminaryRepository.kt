package com.osia.nota_maestro.repository.preliminary

import com.osia.nota_maestro.model.Preliminary
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("preliminary.crud_repository")
interface PreliminaryRepository :
    JpaRepository<Preliminary, UUID>,
    JpaSpecificationExecutor<Preliminary>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM preliminaries", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM preliminaries where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Preliminary>


    @Modifying
    @Transactional
    @Query("UPDATE Preliminary SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidClassroomAndPeriod(classroom: UUID, period: Int): List<Preliminary>
}
