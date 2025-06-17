package com.osia.nota_maestro.factory

import com.github.javafaker.Faker
import com.osia.nota_maestro.dto.post.v1.PostDto
import com.osia.nota_maestro.dto.post.v1.PostRequest
import com.osia.nota_maestro.service.post.PostService
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
