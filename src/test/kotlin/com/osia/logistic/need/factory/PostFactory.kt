package com.osia.logistic.need.factory

import com.github.javafaker.Faker
import com.osia.logistic.need.dto.post.v1.PostDto
import com.osia.logistic.need.dto.post.v1.PostRequest
import com.osia.logistic.need.service.post.PostService
import org.springframework.stereotype.Component

@Component
class PostFactory(
    private val postService: PostService,
    private val clientFactory: ClientFactory
) {
    var faker = Faker()

    fun createRequest(): PostRequest {
        return PostRequest().apply {
            this.clientUuid = clientFactory.create().uuid
            this.description = faker.harryPotter().location()
        }
    }

    fun create(): PostDto {
        return postService.save(createRequest())
    }
}
