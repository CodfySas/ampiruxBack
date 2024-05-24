package com.osia.nota_maestro.repository.postComment

import com.osia.nota_maestro.model.PostComment
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("postComment.crud_repository")
interface PostCommentRepository :
    JpaRepository<PostComment, UUID>,
    JpaSpecificationExecutor<PostComment>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM post_comments", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM post_comments where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<PostComment>

    @Modifying
    @Transactional
    @Query("UPDATE PostComment SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

}
