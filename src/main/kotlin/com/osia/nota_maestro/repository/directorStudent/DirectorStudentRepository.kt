package com.osia.nota_maestro.repository.directorStudent

import com.osia.nota_maestro.model.DirectorStudent
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("directorStudent.crud_repository")
interface DirectorStudentRepository :
    JpaRepository<DirectorStudent, UUID>,
    JpaSpecificationExecutor<DirectorStudent>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM director_students", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM director_students where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<DirectorStudent>

    @Modifying
    @Transactional
    @Query("UPDATE DirectorStudent SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomStudentInAndPeriod(uuids: List<UUID>, period: Int): List<DirectorStudent>
}
