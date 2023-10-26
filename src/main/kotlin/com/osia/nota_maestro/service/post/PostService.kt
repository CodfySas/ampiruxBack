package com.osia.nota_maestro.service.post

import com.osia.nota_maestro.dto.post.v1.PostDto
import com.osia.nota_maestro.dto.post.v1.PostRequest
import com.osia.nota_maestro.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PostService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): Post
    fun findByMultiple(uuidList: List<UUID>): List<PostDto>
    fun findAll(pageable: Pageable): Page<PostDto>
    fun findAllByFilter(pageable: Pageable, where: String): Page<PostDto>
    // Create
    fun save(postRequest: PostRequest): PostDto
    fun saveMultiple(postRequestList: List<PostRequest>): List<PostDto>
    // Update
    fun update(uuid: UUID, postRequest: PostRequest): PostDto
    fun updateMultiple(postDtoList: List<PostDto>): List<PostDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
