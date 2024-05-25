package com.osia.nota_maestro.service.postComment

import com.osia.nota_maestro.dto.postComment.v1.PostCommentDto
import com.osia.nota_maestro.dto.postComment.v1.PostCommentRequest
import com.osia.nota_maestro.model.PostComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PostCommentService {
    // Read
    fun count(increment: Int): Long
    fun getById(uuid: UUID): PostComment
    fun findByMultiple(uuidList: List<UUID>): List<PostCommentDto>
    fun findAll(pageable: Pageable, school: UUID): Page<PostCommentDto>
    fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PostCommentDto>
    // Create
    fun save(postCommentRequest: PostCommentRequest, replace: Boolean = false): PostCommentDto
    fun saveMultiple(postCommentRequestList: List<PostCommentRequest>): List<PostCommentDto>
    // Update
    fun update(uuid: UUID, postCommentRequest: PostCommentRequest, includeDelete: Boolean = false): PostCommentDto
    fun updateMultiple(postCommentDtoList: List<PostCommentDto>): List<PostCommentDto>
    // Delete
    fun delete(uuid: UUID)
    fun deleteMultiple(uuidList: List<UUID>)

    fun comment(postCommentRequest: PostCommentRequest): PostCommentDto
    fun respond(postCommentRequest: PostCommentRequest, school: UUID): PostCommentDto

    fun getComments(pageable: Pageable, post: UUID, user: UUID): Page<PostCommentDto>
    fun getResponses(pageable: Pageable, comment: UUID, user: UUID): Page<PostCommentDto>
}
