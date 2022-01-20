package com.osia.logistic.need.service.post

import com.osia.logistic.need.dto.post.v1.PostDto
import com.osia.logistic.need.dto.post.v1.PostRequest
import com.osia.logistic.need.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.util.UUID

interface PostService {

    fun findByUuid(uuid: UUID): PostDto

    fun findAll(pageable: Pageable, specs: Specification<Post>?): Page<PostDto>

    fun save(postRequest: PostRequest): PostDto

    fun update(uuid: UUID, postRequest: PostRequest): PostDto

    fun delete(uuid: UUID): PostDto

    fun getOne(uuid: UUID): Post

    fun findByUuidIn(postList: List<UUID>): List<PostDto>
}
