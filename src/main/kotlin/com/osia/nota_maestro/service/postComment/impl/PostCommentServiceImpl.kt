package com.osia.nota_maestro.service.postComment.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.nota_maestro.dto.postComment.v1.PostCommentDto
import com.osia.nota_maestro.dto.postComment.v1.PostCommentMapper
import com.osia.nota_maestro.dto.postComment.v1.PostCommentRequest
import com.osia.nota_maestro.model.PostComment
import com.osia.nota_maestro.repository.post.PostRepository
import com.osia.nota_maestro.repository.postComment.PostCommentRepository
import com.osia.nota_maestro.repository.postReact.PostReactRepository
import com.osia.nota_maestro.repository.user.UserRepository
import com.osia.nota_maestro.service.postComment.PostCommentService
import com.osia.nota_maestro.util.CreateSpec
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("postComment.crud_service")
@Transactional
class PostCommentServiceImpl(
    private val postCommentRepository: PostCommentRepository,
    private val postCommentMapper: PostCommentMapper,
    private val objectMapper: ObjectMapper,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val postReactRepository: PostReactRepository
) : PostCommentService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("postComment count -> increment: $increment")
        return postCommentRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): PostComment {
        return postCommentRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "PostComment $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<PostCommentDto> {
        log.trace("postComment findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return postCommentRepository.findAllById(uuidList).map(postCommentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, school: UUID): Page<PostCommentDto> {
        log.trace("postComment findAll -> pageable: $pageable")
        return postCommentRepository.findAll(Specification.where(CreateSpec<PostComment>().createSpec("", school)), pageable).map(postCommentMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String, school: UUID): Page<PostCommentDto> {
        log.trace("postComment findAllByFilter -> pageable: $pageable, where: $where")
        return postCommentRepository.findAll(Specification.where(CreateSpec<PostComment>().createSpec(where, school)), pageable).map(postCommentMapper::toDto)
    }

    @Transactional
    override fun save(postCommentRequest: PostCommentRequest, replace: Boolean): PostCommentDto {
        log.trace("postComment save -> request: $postCommentRequest")
        val savedPostComment = postCommentMapper.toModel(postCommentRequest)
        return postCommentMapper.toDto(postCommentRepository.save(savedPostComment))
    }

    @Transactional
    override fun saveMultiple(postCommentRequestList: List<PostCommentRequest>): List<PostCommentDto> {
        log.trace("postComment saveMultiple -> requestList: ${objectMapper.writeValueAsString(postCommentRequestList)}")
        val postComments = postCommentRequestList.map(postCommentMapper::toModel)
        return postCommentRepository.saveAll(postComments).map(postCommentMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, postCommentRequest: PostCommentRequest, includeDelete: Boolean): PostCommentDto {
        log.trace("postComment update -> uuid: $uuid, request: $postCommentRequest")
        val postComment = if (!includeDelete) {
            getById(uuid)
        } else {
            postCommentRepository.getByUuid(uuid).get()
        }
        postCommentMapper.update(postCommentRequest, postComment)
        return postCommentMapper.toDto(postCommentRepository.save(postComment))
    }

    @Transactional
    override fun updateMultiple(postCommentDtoList: List<PostCommentDto>): List<PostCommentDto> {
        log.trace("postComment updateMultiple -> postCommentDtoList: ${objectMapper.writeValueAsString(postCommentDtoList)}")
        val postComments = postCommentRepository.findAllById(postCommentDtoList.mapNotNull { it.uuid })
        postComments.forEach { postComment ->
            postCommentMapper.update(postCommentMapper.toRequest(postCommentDtoList.first { it.uuid == postComment.uuid }), postComment)
        }
        return postCommentRepository.saveAll(postComments).map(postCommentMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("postComment delete -> uuid: $uuid")
        val postComment = getById(uuid)
        postComment.deleted = true
        postComment.deletedAt = LocalDateTime.now()
        postCommentRepository.save(postComment)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("postComment deleteMultiple -> uuid: $uuidList")
        val postComments = postCommentRepository.findAllById(uuidList)
        postComments.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        postCommentRepository.saveAll(postComments)
    }

    @Transactional
    override fun comment(postCommentRequest: PostCommentRequest): PostCommentDto {
        log.trace("postComment comment -> uuid: ${objectMapper.writeValueAsString(postCommentRequest)}")
        val post = postRepository.getByUuid(postCommentRequest.uuidPost!!)
        post.get().comments++
        post.get().lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        postRepository.save(post.get())
        return save(postCommentRequest)
    }

    @Transactional
    override fun respond(postCommentRequest: PostCommentRequest): PostCommentDto {
        log.trace("postComment respond -> uuid: ${objectMapper.writeValueAsString(postCommentRequest)}")
        val comment = postCommentRepository.getByUuid(postCommentRequest.uuidParent!!)
        comment.get().responses++
        comment.get().lastModifiedAt = LocalDateTime.now(ZoneId.of("America/Bogota"))
        postCommentRepository.save(comment.get())
        return save(postCommentRequest)
    }

    override fun getComments(pageable: Pageable, post: UUID, user: UUID): Page<PostCommentDto> {
        val comments = postCommentRepository.findAll(createSpec(post), pageable).map(postCommentMapper::toDto)
        val users = userRepository.findAllById(comments.mapNotNull { it.uuidUser })
        val myReacts = postReactRepository.findAllByUuidCommentInAndUuidUser(comments.mapNotNull { it.uuid }, user)
        comments.forEach { c->
            val u = users.firstOrNull { u-> u.uuid == c.uuidUser }
            c.userName = "${u?.name} ${u?.lastname}"
            if(u?.role == "admin"){
                c.userRole = "Administrador"
            }
            if(u?.role == "teacher"){
                c.userRole = "Docente"
            }
            if(u?.role == "student"){
                c.userRole = "Estudiante"
            }
            val myR = myReacts.firstOrNull { it.uuidComment == c.uuid }
            if(myR?.react != 0){
                c.selectedReact = myR?.react
            }else{
                c.selectedReact = 0
            }
        }
        return comments
    }

    override fun getResponses(pageable: Pageable, comment: UUID, user: UUID): Page<PostCommentDto> {
        val comments = postCommentRepository.findAll(createSpecComment(comment), pageable).map(postCommentMapper::toDto)
        val users = userRepository.findAllById(comments.mapNotNull { it.uuidUser })
        val myReacts = postReactRepository.findAllByUuidCommentInAndUuidUser(comments.mapNotNull { it.uuid }, user)
        comments.forEach { c->
            val u = users.firstOrNull { u-> u.uuid == c.uuidUser }
            c.userName = "${u?.name} ${u?.lastname}"
            if(u?.role == "admin"){
                c.userRole = "Administrador"
            }
            if(u?.role == "teacher"){
                c.userRole = "Docente"
            }
            if(u?.role == "student"){
                c.userRole = "Estudiante"
            }
            val myR = myReacts.firstOrNull { it.uuidComment == c.uuid }
            if(myR?.react != 0){
                c.selectedReact = myR?.react
            }else{
                c.selectedReact = 0
            }
        }
        return comments
    }

    private fun createSpec(post: UUID): Specification<PostComment> {
        var finalSpec = Specification { root: Root<PostComment>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Boolean>("deleted").`in`(false)
        }
        finalSpec = finalSpec.and { root: Root<PostComment>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<UUID>("isResponse").`in`(false)
        }
        finalSpec = finalSpec.and { root: Root<PostComment>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<UUID>("uuidPost").`in`(post)
        }
        return finalSpec
    }

    private fun createSpecComment(comment: UUID): Specification<PostComment> {
        var finalSpec = Specification { root: Root<PostComment>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Boolean>("deleted").`in`(false)
        }
        finalSpec = finalSpec.and { root: Root<PostComment>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<UUID>("uuidParent").`in`(comment)
        }
        return finalSpec
    }
}
