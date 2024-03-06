package com.osia.nota_maestro.repository.director

import com.osia.nota_maestro.model.Director
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("director.crud_repository")
interface DirectorRepository :
    JpaRepository<Director, UUID>,
    JpaSpecificationExecutor<Director>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM directors", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM directors where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<Director>

    @Modifying
    @Transactional
    @Query("UPDATE Director SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomIn(classrooms: List<UUID>): List<Director>

    fun getAllByUuidTeacher(uuid: UUID): List<Director>

    fun findFirstByUuidClassroom(uuid: UUID): Optional<Director>
}
