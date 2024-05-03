package com.osia.nota_maestro.repository.examUserResponse

import com.osia.nota_maestro.model.ExamUserResponse
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("examUserResponse.crud_repository")
interface ExamUserResponseRepository :
    JpaRepository<ExamUserResponse, UUID>,
    JpaSpecificationExecutor<ExamUserResponse>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM exam_user_responses", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM exam_user_responses where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<ExamUserResponse>

    @Modifying
    @Transactional
    @Query("UPDATE ExamUserResponse SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)
}
