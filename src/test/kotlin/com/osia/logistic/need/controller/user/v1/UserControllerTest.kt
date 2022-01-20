package com.osia.logistic.need.controller.user.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.logistic.need.config.ExternalServiceMock
import com.osia.logistic.need.factory.UserFactory
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
internal class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userFactory: UserFactory

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var coverageServiceMock = ExternalServiceMock(8090)

    private var uri: String = "/v1/users"

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
            userFactory.create()
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
        val userDto = userFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${userDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(userDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(userDto.name)),
                )
            )
    }

    @Test
    fun create() {
        val userRequest = userFactory.createRequest()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(userRequest.name)),
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                )
            )
            .andReturn().response.contentAsString
    }

    @Test
    fun update() {
        val userDto = userFactory.create()

        val userRequest = userFactory.createRequest()

        this.mockMvc.perform(
            MockMvcRequestBuilders.patch("$uri/${userDto.uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(userDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(userRequest.name)),
                )
            )
    }

    @Test
    fun delete() {
        val userDto = userFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${userDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(userDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(userDto.name)),
                )
            )

        this.mockMvc.perform(MockMvcRequestBuilders.delete("$uri/${userDto.uuid}"))

        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${userDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isNotFound
                )
            )
    }

    @Test
    fun showMultiple() {
        val userDto = userFactory.create()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("$uri/multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutableListOf(userDto.uuid)))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.[0].uuid", Matchers.equalTo(userDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.[0].name", Matchers.equalTo(userDto.name)),
                )
            )
    }
}
