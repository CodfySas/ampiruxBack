package com.osia.osia_erp.controller.post.v1

import com.osia.osia_erp.dto.post.v1.PostMapper
import com.osia.osia_erp.service.post.PostService
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
