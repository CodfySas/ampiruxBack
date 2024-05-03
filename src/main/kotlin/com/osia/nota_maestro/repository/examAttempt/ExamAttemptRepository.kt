package com.osia.nota_maestro.repository.examAttempt

import com.osia.nota_maestro.model.ExamAttempt
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("examAttempt.crud_repository")
interface ExamAttemptRepository :
    JpaRepository<ExamAttempt, UUID>,
    JpaSpecificationExecutor<ExamAttempt>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM exam_attempts", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM exam_attempts where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ExamAttempt>

    @Modifying
    @Transactional
    @Query("UPDATE ExamAttempt SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findAllByUuidStudentAndUuidExam(uuid: UUID, exam: UUID): List<ExamAttempt>
}
