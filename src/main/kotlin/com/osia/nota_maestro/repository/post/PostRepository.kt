package com.osia.nota_maestro.repository.post

import com.osia.nota_maestro.model.Post
import com.osia.nota_maestro.repository.BaseRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("post.crud_repository")
interface PostRepository :
    JpaRepository<Post, UUID>,
    JpaSpecificationExecutor<Post>,
    BaseRepository {

    @Query(value = "SELECT COUNT(*) FROM posts", nativeQuery = true)
    override fun count(increment: Int): Long
}
