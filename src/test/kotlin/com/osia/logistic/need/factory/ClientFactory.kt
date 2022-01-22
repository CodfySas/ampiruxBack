package com.osia.logistic.need.factory

import com.github.javafaker.Faker
import com.osia.logistic.need.dto.client.v1.ClientDto
import com.osia.logistic.need.dto.client.v1.ClientRequest
import com.osia.logistic.need.service.client.ClientService
import org.springframework.stereotype.Component

@Component
class ClientFactory(
    private val clientService: ClientService
) {
    var faker = Faker()

    fun createRequest(): ClientRequest {
        return ClientRequest().apply {
            this.name = faker.name().fullName()
            this.email = faker.internet().emailAddress()
            this.password = faker.internet().password()
        }
    }

    fun create(): ClientDto {
        return clientService.save(createRequest())
    }
}
