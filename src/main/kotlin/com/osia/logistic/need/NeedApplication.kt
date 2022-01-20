package com.osia.logistic.need

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableFeignClients
@EnableRetry

class NeedApplication

fun main(args: Array<String>) { runApplication<NeedApplication>(*args) }
