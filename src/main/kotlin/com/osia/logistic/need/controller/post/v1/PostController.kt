package com.osia.logistic.need.controller.post.v1

import com.osia.logistic.need.dto.post.v1.PostDto
import com.osia.logistic.need.dto.post.v1.PostRequest
import com.osia.logistic.need.dto.request.OnCreate
import com.osia.logistic.need.model.Post
import com.osia.logistic.need.service.post.PostService
import com.sipios.springsearch.anotation.SearchSpec
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("post.v1.crud")
@RequestMapping("v1/posts")
@Api(tags = ["posts", "crud"])
@Validated
class PostController(
    private val postService: PostService,
) {

    @GetMapping
    fun index(pageable: Pageable, @SearchSpec specs: Specification<Post>?): Page<PostDto> {
        return postService.findAll(pageable, specs)
    }

    @GetMapping("/{uuid}")
    fun show(@PathVariable uuid: UUID): ResponseEntity<PostDto> {
        return ResponseEntity.ok(postService.findByUuid(uuid))
    }

    @PostMapping
    fun create(
        @Validated(OnCreate::class) @RequestBody request: PostRequest
    ): ResponseEntity<PostDto> {
        return ResponseEntity.ok(postService.save(request))
    }

    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PostRequest
    ): ResponseEntity<PostDto> {
        return ResponseEntity.ok(postService.update(uuid, request))
    }

    @DeleteMapping("/{uuid}")
    fun delete(@PathVariable uuid: UUID): ResponseEntity<PostDto> {
        return ResponseEntity.ok(postService.delete(uuid))
    }

    @PostMapping("/multiple")
    fun findByMany(@RequestBody postList: List<UUID>): List<PostDto> {
        return postService.findByUuidIn(postList)
    }
}
