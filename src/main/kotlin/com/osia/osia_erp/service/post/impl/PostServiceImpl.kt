package com.osia.osia_erp.service.post.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.osia_erp.dto.post.v1.PostDto
import com.osia.osia_erp.dto.post.v1.PostMapper
import com.osia.osia_erp.dto.post.v1.PostRequest
import com.osia.osia_erp.model.Post
import com.osia.osia_erp.repository.post.PostRepository
import com.osia.osia_erp.service.post.PostService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

@Service("post.crud_service")
@Transactional
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val postMapper: PostMapper,
    private val objectMapper: ObjectMapper
) : PostService {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(readOnly = true)
    override fun count(increment: Int): Long {
        log.trace("post count -> increment: $increment")
        return postRepository.count() + increment
    }

    @Transactional(readOnly = true)
    override fun getById(uuid: UUID): Post {
        return postRepository.findById(uuid).orElseThrow {
            ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Post $uuid not found")
        }
    }

    @Transactional(readOnly = true)
    override fun findByMultiple(uuidList: List<UUID>): List<PostDto> {
        log.trace("post findByMultiple -> uuidList: ${objectMapper.writeValueAsString(uuidList)}")
        return postRepository.findAllById(uuidList).map(postMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<PostDto> {
        log.trace("post findAll -> pageable: $pageable")
        return postRepository.findAll(pageable).map(postMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findAllByFilter(pageable: Pageable, where: String): Page<PostDto> {
        log.trace("post findAllByFilter -> pageable: $pageable, where: $where")
        return postRepository.findAll(Specification.where(createSpec(where)), pageable).map(postMapper::toDto)
    }

    @Transactional
    override fun save(postRequest: PostRequest): PostDto {
        log.trace("post save -> request: $postRequest")
        val post = postMapper.toModel(postRequest)
        return postMapper.toDto(postRepository.save(post))
    }

    @Transactional
    override fun saveMultiple(postRequestList: List<PostRequest>): List<PostDto> {
        log.trace("post saveMultiple -> requestList: ${objectMapper.writeValueAsString(postRequestList)}")
        val posts = postRequestList.map(postMapper::toModel)
        return postRepository.saveAll(posts).map(postMapper::toDto)
    }

    @Transactional
    override fun update(uuid: UUID, postRequest: PostRequest): PostDto {
        log.trace("post update -> uuid: $uuid, request: $postRequest")
        val post = getById(uuid)
        postMapper.update(postRequest, post)
        return postMapper.toDto(postRepository.save(post))
    }

    @Transactional
    override fun updateMultiple(postDtoList: List<PostDto>): List<PostDto> {
        log.trace("post updateMultiple -> postDtoList: ${objectMapper.writeValueAsString(postDtoList)}")
        val posts = postRepository.findAllById(postDtoList.mapNotNull { it.uuid })
        posts.forEach { post ->
            postMapper.update(postMapper.toRequest(postDtoList.first { it.uuid == post.uuid }), post)
        }
        return postRepository.saveAll(posts).map(postMapper::toDto)
    }

    @Transactional
    override fun delete(uuid: UUID) {
        log.trace("post delete -> uuid: $uuid")
        val post = getById(uuid)
        post.deleted = true
        post.deletedAt = LocalDateTime.now()
        postRepository.save(post)
    }

    @Transactional
    override fun deleteMultiple(uuidList: List<UUID>) {
        log.trace("post deleteMultiple -> uuid: $uuidList")
        val posts = postRepository.findAllById(uuidList)
        posts.forEach {
            it.deleted = true
            it.deletedAt = LocalDateTime.now()
        }
        postRepository.saveAll(posts)
    }

    fun createSpec(where: String): Specification<Post> {
        var finalSpec = Specification { root: Root<Post>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
            root.get<Any>("deleted").`in`(false)
        }
        where.split(",").forEach {
            finalSpec = finalSpec.and { root: Root<Post>, _: CriteriaQuery<*>?, _: CriteriaBuilder ->
                root.get<Any>(it.split(":")[0]).`in`(it.split(":")[1])
            }
        }
        return finalSpec
    }
}
