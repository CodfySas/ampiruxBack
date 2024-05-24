package com.osia.nota_maestro.controller.postReact.v1

import com.osia.nota_maestro.dto.OnCreate
import com.osia.nota_maestro.dto.postReact.v1.PostReactComplete
import com.osia.nota_maestro.dto.postReact.v1.PostReactDto
import com.osia.nota_maestro.dto.postReact.v1.PostReactMapper
import com.osia.nota_maestro.dto.postReact.v1.PostReactRequest
import com.osia.nota_maestro.service.postReact.PostReactService
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

@RestController("postReact.v1.crud")
@CrossOrigin
@RequestMapping("v1/post-reacts")
@Validated
class PostReactController(
    private val postReactService: PostReactService,
    private val postReactMapper: PostReactMapper
) {
    // Read
    @GetMapping
    fun findAll(pageable: Pageable, @RequestHeader school: UUID): Page<PostReactDto> {
        return postReactService.findAll(pageable, school)
    }

    @GetMapping("/filter/{where}")
    fun findAllByFilter(
        pageable: Pageable,
        @PathVariable where: String,
        @RequestHeader school: UUID
    ): Page<PostReactDto> {
        return postReactService.findAllByFilter(pageable, where, school)
    }

    @GetMapping("/count/{increment}")
    fun count(@PathVariable increment: Int): Long {
        return postReactService.count(increment)
    }

    @GetMapping("/{uuid}")
    fun getById(@PathVariable uuid: UUID): ResponseEntity<PostReactDto> {
        return ResponseEntity.ok().body(postReactMapper.toDto(postReactService.getById(uuid)))
    }

    @GetMapping("/multiple")
    fun getByMultipleId(@RequestBody uuidList: List<UUID>): ResponseEntity<List<PostReactDto>> {
        return ResponseEntity.ok().body(postReactService.findByMultiple(uuidList))
    }

    // Create
    @PostMapping
    fun save(
        @Validated(OnCreate::class) @RequestBody request: PostReactRequest
    ): ResponseEntity<PostReactDto> {
        return ResponseEntity(postReactService.save(request), HttpStatus.CREATED)
    }

    @PostMapping("/multiple")
    fun saveMultiple(
        @Validated(OnCreate::class) @RequestBody postReactRequestList: List<PostReactRequest>
    ): ResponseEntity<List<PostReactDto>> {
        return ResponseEntity(postReactService.saveMultiple(postReactRequestList), HttpStatus.CREATED)
    }

    // update
    @PatchMapping("/{uuid}")
    fun update(
        @PathVariable uuid: UUID,
        @RequestBody request: PostReactRequest
    ): ResponseEntity<PostReactDto> {
        return ResponseEntity.ok().body(postReactService.update(uuid, request))
    }

    @PatchMapping("/multiple")
    fun updateMultiple(
        @RequestBody postReactDtoList: List<PostReactDto>
    ): ResponseEntity<List<PostReactDto>> {
        return ResponseEntity.ok().body(postReactService.updateMultiple(postReactDtoList))
    }

    // delete
    @DeleteMapping("/{uuid}")
    fun delete(
        @PathVariable uuid: UUID,
    ): ResponseEntity<HttpStatus> {
        postReactService.delete(uuid)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/multiple")
    fun deleteMultiple(
        @RequestBody uuidList: List<UUID>
    ): ResponseEntity<HttpStatus> {
        postReactService.deleteMultiple(uuidList)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/react/{post}/{react}")
    fun react(
        @PathVariable post: UUID,
        @PathVariable react: Int,
        @RequestHeader user: UUID
    ): ResponseEntity<PostReactDto> {
        return ResponseEntity.ok(postReactService.reactClick(post, react, user))
    }

    @PostMapping("/react-comment/{comment}/{post}/{react}")
    fun reactComment(
        @PathVariable comment: UUID,
        @PathVariable post: UUID,
        @PathVariable react: Int,
        @RequestHeader user: UUID
    ): ResponseEntity<PostReactDto> {
        return ResponseEntity.ok(postReactService.reactComment(comment, post, react, user))
    }

    @PostMapping("/react-response/{response}/{post}/{comment}/{react}")
    fun reactResponse(
        @PathVariable response: UUID,
        @PathVariable comment: UUID,
        @PathVariable post: UUID,
        @PathVariable react: Int,
        @RequestHeader user: UUID
    ): ResponseEntity<PostReactDto> {
        return ResponseEntity.ok(postReactService.reactResponse(response, post, comment, react, user))
    }

    @GetMapping("/get-reacts/{post}")
    fun getReacts(@PathVariable post: UUID): ResponseEntity<List<PostReactComplete>> {
        return ResponseEntity.ok(postReactService.getReacts(post))
    }

}
