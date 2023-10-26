package com.osia.nota_maestro.controller.post.v1

import com.osia.nota_maestro.dto.post.v1.PostMapper
import com.osia.nota_maestro.service.post.PostService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("post.v1.crud")
@RequestMapping("v1/posts")
@Validated
class PostController(
    private val postService: PostService,
    private val postMapper: PostMapper
)
