package com.osia.nota_maestro.service.postReact

import com.osia.nota_maestro.dto.postReact.v1.PostReactComplete
import com.osia.nota_maestro.dto.postReact.v1.PostReactDto
import com.osia.nota_maestro.dto.postReact.v1.PostReactRequest
import com.osia.nota_maestro.model.PostReact
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PostReactService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): PostReact
    fun findByMultiple(uuidList: List<UUID>): List<PostReactDto>
    fun findAll(pageable: Pageable, school: UUID): Page<PostReactDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PostReactDto>
    // Create
    fun save(postReactRequest: PostReactRequest, replace: Boolean = false): PostReactDto
    fun saveMultiple(postReactRequestList: List<PostReactRequest>): List<PostReactDto>
    // Update
    fun update(uuid: UUID, postReactRequest: PostReactRequest, includeDelete: Boolean = false): PostReactDto
    fun updateMultiple(postReactDtoList: List<PostReactDto>): List<PostReactDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun reactClick(post: UUID, react: Int, user: UUID): PostReactDto
    fun reactComment(comment: UUID, post: UUID, react: Int, user: UUID): PostReactDto
    fun reactResponse(response: UUID, post: UUID, comment: UUID, react: Int, user: UUID): PostReactDto

    fun getReacts(uuid: UUID, type: String): List<PostReactComplete>
}
