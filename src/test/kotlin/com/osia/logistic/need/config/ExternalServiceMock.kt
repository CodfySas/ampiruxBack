package com.osia.logistic.need.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class ExternalServiceMock(private val port: Int) : WireMockServer(port) {

    fun startMockServer() {
        start()
        stubFor(
            WireMock.get(WireMock.urlPathMatching("/v1/addresses.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(ClassPathResource("mock/example-response.json").file.readText())
                )
        )
    } // test
}
