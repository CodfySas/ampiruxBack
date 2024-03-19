package com.osia.nota_maestro.repository.examResponse

import com.osia.nota_maestro.model.ExamResponse
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("examResponse.crud_repository")
interface ExamResponseRepository :
    JpaRepository<ExamResponse, UUID>,
    JpaSpecificationExecutor<ExamResponse>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM exam_responses", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM exam_responses where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ExamResponse>

    @Modifying
    @Transactional
    @Query("UPDATE ExamResponse SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun getAllByUuidExamQuestionIn(uuids: List<UUID>): List<ExamResponse>
}
