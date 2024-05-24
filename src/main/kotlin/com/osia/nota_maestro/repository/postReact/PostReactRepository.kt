package com.osia.nota_maestro.repository.postReact

import com.osia.nota_maestro.model.PostReact
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository("postReact.crud_repository")
interface PostReactRepository :
    JpaRepository<PostReact, UUID>,
    JpaSpecificationExecutor<PostReact>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM post_reacts", nativeQuery = true)
    override fun count(schoolUuid: UUID?): Long

    @Query(value = "SELECT * FROM post_reacts where uuid = ?1", nativeQuery = true)
    fun getByUuid(uuid: UUID): Optional<PostReact>

    @Modifying
    @Transactional
    @Query("UPDATE PostReact SET deleted = true, deletedAt = now() WHERE uuid IN :uuids")
    fun deleteByUuids(uuids: List<UUID>)

    fun findFirstByUuidPostAndUuidUserAndUuidCommentIsNull(post: UUID, user: UUID): Optional<PostReact>

    fun findFirstByUuidCommentAndUuidUser(post: UUID, user: UUID): Optional<PostReact>

    fun findAllByUuidPostInAndUuidUserAndUuidCommentIsNull(posts: List<UUID>, user: UUID): List<PostReact>

    fun findAllByUuidPostAndReactNotAndUuidCommentIsNull(post: UUID, react: Int): List<PostReact>

    fun findAllByUuidCommentInAndUuidUser(posts: List<UUID>, user: UUID): List<PostReact>
}
