package com.osia.logistic.need.service.post.impl

import com.osia.logistic.need.dto.post.v1.PostDto
import com.osia.logistic.need.dto.post.v1.PostMapper
import com.osia.logistic.need.dto.post.v1.PostRequest
import com.osia.logistic.need.model.Post
import com.osia.logistic.need.repository.post.PostRepository
import com.osia.logistic.need.service.post.PostService
import com.osia.logistic.need.service.user.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service("post.crud_service")
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val postMapper: PostMapper,
) : PostService {

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, specs: Specification<Post>?): Page<PostDto> {
        return postRepository.findAll(Specification.where(specs), pageable).map(postMapper::toDto)
    }

    @Transactional(readOnly = true)
    override fun findByUuid(uuid: UUID): PostDto {
        return postMapper.toDto(getOne(uuid))
    }

    @Transactional
    override fun save(postRequest: PostRequest): PostDto {
        val post = postMapper.toModel(postRequest)
        post.user = userService.getOne(postRequest.userUuid ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST))
        return postMapper.toDto(postRepository.save(post))
    }

    @Transactional
    override fun update(uuid: UUID, postRequest: PostRequest): PostDto {
        val post = getOne(uuid)
        postMapper.updateModel(postRequest, post)
        post.user = userService.getOne(postRequest.userUuid ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST))
        return postMapper.toDto(postRepository.save(post))
    }

    override fun delete(uuid: UUID): PostDto {
        val post = getOne(uuid)
        val postDto = postMapper.toDto(post)
        postRepository.delete(post)
        return postDto
    }

    override fun findByUuidIn(postList: List<UUID>): List<PostDto> {
        return postRepository.findByUuidIn(postList).map(postMapper::toDto)
    }

    override fun getOne(uuid: UUID): Post {
        return postRepository.findById(uuid).orElseThrow {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "post $uuid not found")
        }
    }
}
