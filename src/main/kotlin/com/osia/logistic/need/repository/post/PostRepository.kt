package com.osia.logistic.need.repository.post

import com.osia.logistic.need.model.Post
import com.osia.logistic.need.repository.baseRepository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("post.crud_repository")
interface PostRepository : JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post>, BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM post", nativeQuery = true)
    override fun countAll(): Long

    fun findByUuidIn(list: List<UUID>): List<Post>
}
