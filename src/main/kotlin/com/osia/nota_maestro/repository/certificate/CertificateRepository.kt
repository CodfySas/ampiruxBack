package com.osia.nota_maestro.repository.certificate

import com.osia.nota_maestro.model.Certificate
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("certificate.crud_repository")
interface CertificateRepository :
    JpaRepository<Certificate, UUID>,
    JpaSpecificationExecutor<Certificate>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM certificates", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM certificates where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Certificate>

    @Modifying
    @Transactional
    @Query("UPDATE Certificate SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidUser(uuid: UUID): List<Certificate>

    fun findAllByUuidSchoolAndStatus(uuid: UUID, status: String): List<Certificate>
}
