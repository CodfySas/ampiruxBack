package com.osia.nota_maestro.controller.post.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.post.v1.PostDto
import com.osia.nota_maestro.dto.post.v1.PostMapper
import com.osia.nota_maestro.dto.post.v1.PostRequest
import com.osia.nota_maestro.service.post.PostService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController("post.v1.crud")
@CrossOrigin
@RequestMapping("v1/posts")
@Validated
class PostController(
    private val postService: PostService,
    private val postMapper: PostMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID, @RequestHeader user: UUID): Page<PostDto> {
        return postService.findAll(pageable, school, user)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<PostDto> {
        return postService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return postService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<PostDto> {
        return ResponseEntity.ok().body(postMapper.toDto(postService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<PostDto>> {
        return ResponseEntity.ok().body(postService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: PostRequest, @RequestHeader school: UUID
    ): ResponseEntity<PostDto> {
        request.uuidSchool = school
        return ResponseEntity(postService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody postRequestList: List<PostRequest>
    ): ResponseEntity<List<PostDto>> {
        return ResponseEntity(postService.saveMultiple(postRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PostRequest
    ): ResponseEntity<PostDto> {
        return ResponseEntity.ok().body(postService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody postDtoList: List<PostDto>
    ): ResponseEntity<List<PostDto>> {
        return ResponseEntity.ok().body(postService.updateMultiple(postDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        postService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        postService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }
}
