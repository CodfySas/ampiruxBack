package com.osia.nota_maestro.repository.mesh

import com.osia.nota_maestro.model.Mesh
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("mesh.crud_repository")
interface MeshRepository :
    JpaRepository<Mesh, UUID>,
    JpaSpecificationExecutor<Mesh>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM meshs", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM meshs where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Mesh>

    @Modifying
    @Transactional
    @Query("UPDATE Mesh SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByClassroomAndSubjectAndPeriod(classroom: UUID, subject: UUID, period: Int): List<Mesh>
}
