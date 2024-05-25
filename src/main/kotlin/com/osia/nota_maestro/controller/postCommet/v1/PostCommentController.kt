package com.osia.nota_maestro.controller.postCommet.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.postComment.v1.PostCommentDto
import com.osia.nota_maestro.dto.postComment.v1.PostCommentMapper
import com.osia.nota_maestro.dto.postComment.v1.PostCommentRequest
import com.osia.nota_maestro.service.postComment.PostCommentService
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

@RestController("postComment.v1.crud")
@CrossOrigin
@RequestMapping("v1/post-comments")
@Validated
class PostCommentController(
    private val postCommentService: PostCommentService,
    private val postCommentMapper: PostCommentMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<PostCommentDto> {
        return postCommentService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(pageable: Pageable, @PathVariable where: String, @RequestHeader school: UUID): Page<PostCommentDto> {
        return postCommentService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return postCommentService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<PostCommentDto> {
        return ResponseEntity.ok().body(postCommentMapper.toDto(postCommentService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<PostCommentDto>> {
        return ResponseEntity.ok().body(postCommentService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: PostCommentRequest
    ): ResponseEntity<PostCommentDto> {
        return ResponseEntity(postCommentService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody postCommentRequestList: List<PostCommentRequest>
    ): ResponseEntity<List<PostCommentDto>> {
        return ResponseEntity(postCommentService.saveMultiple(postCommentRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PostCommentRequest
    ): ResponseEntity<PostCommentDto> {
        return ResponseEntity.ok().body(postCommentService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody postCommentDtoList: List<PostCommentDto>
    ): ResponseEntity<List<PostCommentDto>> {
        return ResponseEntity.ok().body(postCommentService.updateMultiple(postCommentDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        postCommentService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        postCommentService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/get-comments/{post}")
    fun getComments(pageable: Pageable, @PathVariable post: UUID, @RequestHeader user: UUID): ResponseEntity<Page<PostCommentDto>>{
        return ResponseEntity.ok(postCommentService.getComments(pageable, post, user))
    }

    @GetMapping("/get-responses/{comment}")
    fun getResponses(pageable: Pageable, @PathVariable comment: UUID, @RequestHeader user: UUID): ResponseEntity<Page<PostCommentDto>>{
        return ResponseEntity.ok(postCommentService.getResponses(pageable, comment, user))
    }

    @PostMapping("/comment")
    fun comment(@RequestBody req: PostCommentRequest): ResponseEntity<PostCommentDto>{
        return ResponseEntity.ok(postCommentService.comment(req))
    }

    @PostMapping("/respond")
    fun respond(@RequestBody req: PostCommentRequest, @RequestHeader school: UUID): ResponseEntity<PostCommentDto>{
        return ResponseEntity.ok(postCommentService.respond(req, school))
    }
}
