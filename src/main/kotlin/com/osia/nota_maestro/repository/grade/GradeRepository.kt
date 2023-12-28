package com.osia.nota_maestro.repository.grade

import com.osia.nota_maestro.model.Grade
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("grade.crud_repository")
interface GradeRepository :
    JpaRepository<Grade, UUID>,
    JpaSpecificationExecutor<Grade>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM grades", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    @Query(value = "SELECT * FROM grades where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Grade>

    @Modifying
    @Transactional
    @Query("UPDATE Grade SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
