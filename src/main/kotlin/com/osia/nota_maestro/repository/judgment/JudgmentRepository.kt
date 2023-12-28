package com.osia.nota_maestro.repository.judgment

import com.osia.nota_maestro.model.Judgment
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("judgment.crud_repository")
interface JudgmentRepository :
    JpaRepository<Judgment, UUID>,
    JpaSpecificationExecutor<Judgment>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM judgments", nativeQuery = true)
    override fun count(schoolUuid: UUID): Long

    @Query(value = "SELECT * FROM judgments where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Judgment>

    fun findAllByUuidClassroomStudentIn(uuids: List<UUID>): List<Judgment>

    @Modifying
    @Transactional
    @Query("UPDATE Judgment SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
