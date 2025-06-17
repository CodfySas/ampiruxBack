package com.osia.ampirux.factory

import com.github.javafaker.Faker
import com.osia.ampirux.dto.post.v1.PostDto
import com.osia.ampirux.dto.post.v1.PostRequest
import com.osia.ampirux.service.post.PostService
import org.springframework.stereotype.Component

@Component
class PostFactory(
    private val postService: PostService,
) {
    var faker = Faker()

    fun createRequest(): PostRequest {
        return PostRequest().apply {
            this.description = faker.harryPotter().location()
        }
    }

    fun create(): PostDto {
        return postService.save(createRequest())
    }
}
