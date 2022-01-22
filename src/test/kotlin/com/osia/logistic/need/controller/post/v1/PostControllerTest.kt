package com.osia.logistic.need.controller.post.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.osia.logistic.need.config.ExternalServiceMock
import com.osia.logistic.need.factory.PostFactory
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
internal class PostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var postFactory: PostFactory

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private var coverageServiceMock = ExternalServiceMock(8090)

    private var uri: String = "/v1/posts"

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
            postFactory.create()
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
        val postDto = postFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${postDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(postDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.client_dto.name", Matchers.equalTo(postDto.clientDto.name)),
                    MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(postDto.description)),
                )
            )
    }

    @Test
    fun create() {
        val postRequest = postFactory.createRequest()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(postRequest.description)),
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                )
            )
            .andReturn().response.contentAsString
    }

    @Test
    fun update() {
        val postDto = postFactory.create()

        val postRequest = postFactory.createRequest()

        this.mockMvc.perform(
            MockMvcRequestBuilders.patch("$uri/${postDto.uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(postDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(postRequest.description)),
                )
            )
    }

    @Test
    fun delete() {
        val postDto = postFactory.create()
        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${postDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.uuid", Matchers.equalTo(postDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(postDto.description)),
                )
            )

        this.mockMvc.perform(MockMvcRequestBuilders.delete("$uri/${postDto.uuid}"))

        this.mockMvc.perform(MockMvcRequestBuilders.get("$uri/${postDto.uuid}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isNotFound
                )
            )
    }

    @Test
    fun showMultiple() {
        val postDto = postFactory.create()
        this.mockMvc.perform(
            MockMvcRequestBuilders.post("$uri/multiple")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mutableListOf(postDto.uuid)))
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(
                ResultMatcher.matchAll(
                    MockMvcResultMatchers.status().isOk,
                    MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                    MockMvcResultMatchers.jsonPath("$.[0].uuid", Matchers.equalTo(postDto.uuid.toString())),
                    MockMvcResultMatchers.jsonPath("$.[0].client_dto.name", Matchers.equalTo(postDto.clientDto.name)),
                    MockMvcResultMatchers.jsonPath("$.[0].description", Matchers.equalTo(postDto.description)),
                )
            )
    }
}
