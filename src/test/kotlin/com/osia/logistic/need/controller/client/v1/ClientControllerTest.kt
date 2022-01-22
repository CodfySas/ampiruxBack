package com.osia.logistic.need.controller.client.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.logistic.need.config.ExternalServiceMock
import com.osia.logistic.need.factory.ClientFactory
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ClientControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var clientFactory: ClientFactory

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var coverageServiceMock = ExternalServiceMock(8090)

    private var uri: String = "/v1/clients"

    @BeforeAll
    fun loadMock() {
        coverageServiceMock.startMockServer()
    }

    @AfterAll
    fun shutDownMock() {
        coverageServiceMock.stop()
    }

    @Test
    fun index() {
        for (i in 10 downTo 1 step 1) {
            clientFactory.create()
        }
        this.mockMvc.perform(MockMvcRequestBuilders.get(uri))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.content[0].uuid", Matchers.notNullValue()),
                )
            )
    }

    @Test
    fun show() {
        val clientDto = clientFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${clientDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(clientDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientDto.name)),
                )
            )
    }

    @Test
    fun create() {
        val clientRequest = clientFactory.createRequest()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientRequest.name)),
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                )
            )
            .andReturn().response.contentAsString
    }

    @Test
    fun update() {
        val clientDto = clientFactory.create()

        val clientRequest = clientFactory.createRequest()

        this.mockMvc.perform(
            MockMvcRequestBuilders.patch("$uri/${clientDto.uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(clientDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientRequest.name)),
                )
            )
    }

    @Test
    fun delete() {
        val clientDto = clientFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${clientDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(clientDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(clientDto.name)),
                )
            )

        this.mockMvc.perform(MockMvcRequestBuilders.delete("$uri/${clientDto.uuid}"))

        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${clientDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isNotFound
                )
            )
    }

    @Test
    fun showMultiple() {
        val clientDto = clientFactory.create()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("$uri/multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutableListOf(clientDto.uuid)))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.[0].uuid", Matchers.equalTo(clientDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.equalTo(clientDto.name)),
                )
            )
    }
}
