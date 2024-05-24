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
    fun findAll(pageable: Pageable, school: UUID, user: UUID): Page<PostDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PostDto>
    // Create
    fun save(postRequest: PostRequest, replace: Boolean = false): PostDto
    fun saveMultiple(postRequestList: List<PostRequest>): List<PostDto>
    // Update
    fun update(uuid: UUID, postRequest: PostRequest, includeDelete: Boolean = false): PostDto
    fun updateMultiple(postDtoList: List<PostDto>): List<PostDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)
}
