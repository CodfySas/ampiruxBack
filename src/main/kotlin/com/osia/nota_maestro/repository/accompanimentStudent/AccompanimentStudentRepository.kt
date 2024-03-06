package com.osia.nota_maestro.repository.accompanimentStudent

import com.osia.nota_maestro.model.AccompanimentStudent
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("accompanimentStudent.crud_repository")
interface AccompanimentStudentRepository :
    JpaRepository<AccompanimentStudent, UUID>,
    JpaSpecificationExecutor<AccompanimentStudent>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM accompaniment_students", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM accompaniment_students where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<AccompanimentStudent>

    @Modifying
    @Transactional
    @Query("UPDATE AccompanimentStudent SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidClassroomStudentInAndPeriod(uuids: List<UUID>, period: Int): List<AccompanimentStudent>

    fun getAllByUuidClassroomStudent(uuid: UUID): List<AccompanimentStudent>

    fun getAllByUuidClassroomStudentIn(uuids: List<UUID>): List<AccompanimentStudent>
}
