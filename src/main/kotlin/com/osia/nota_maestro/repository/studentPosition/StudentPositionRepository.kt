package com.osia.nota_maestro.repository.studentPosition

import com.osia.nota_maestro.model.StudentPosition
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("studentPosition.crud_repository")
interface StudentPositionRepository :
    JpaRepository<StudentPosition, UUID>,
    JpaSpecificationExecutor<StudentPosition>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM student_positions", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM student_positions where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<StudentPosition>

    @Modifying
    @Transactional
    @Query("UPDATE StudentPosition SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidClassroomStudentIn(uuids: List<UUID>): List<StudentPosition>
}
