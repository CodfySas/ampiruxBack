package com.osia.nota_maestro.repository.diagnosis

import com.osia.nota_maestro.model.Diagnosis
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("diagnosis.crud_repository")
interface DiagnosisRepository :
    JpaRepository<Diagnosis, UUID>,
    JpaSpecificationExecutor<Diagnosis>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM diagnoses", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM diagnoses where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Diagnosis>

    @Modifying
    @Transactional
    @Query("UPDATE Diagnosis SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidSchool(uuid: UUID): List<Diagnosis>
}
