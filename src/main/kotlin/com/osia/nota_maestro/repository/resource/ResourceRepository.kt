package com.osia.nota_maestro.repository.resource

import com.osia.nota_maestro.model.Resource
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("resource.crud_repository")
interface ResourceRepository :
    JpaRepository<Resource, UUID>,
    JpaSpecificationExecutor<Resource>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM resources", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM resources where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Resource>

    @Query(value = "SELECT * FROM resources where uuid in ?1", nativeQuery = true)
    fun getByUuidIn(uuids: List<UUID>): List<Resource>

    @Modifying
    @Transactional
    @Query("UPDATE Resource SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
