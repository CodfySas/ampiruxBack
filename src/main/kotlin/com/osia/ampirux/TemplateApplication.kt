package com.osia.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableFeignClients
@EnableRetry

class TemplateApplication

fun main(args: Array<String>) { runApplication<com.osia.template.TemplateApplication>(*args) }
