package com.osia.logistic.need.factory

import com.github.javafaker.Faker
import com.osia.logistic.need.dto.user.v1.UserDto
import com.osia.logistic.need.dto.user.v1.UserRequest
import com.osia.logistic.need.service.user.UserService
import org.springframework.stereotype.Component

@Component
class UserFactory(
    private val userService: UserService
) {
    var faker = Faker()

    fun createRequest(): UserRequest {
        return UserRequest().apply {
            this.name = faker.name().fullName()
            this.email = faker.internet().emailAddress()
            this.password = faker.internet().password()
        }
    }

    fun create(): UserDto {
        return userService.save(createRequest())
    }
}
