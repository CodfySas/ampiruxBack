package com.osia.osia_erp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableFeignClients
@EnableRetry

class OsiaErpApplication

fun main(args: Array<String>) { runApplication<com.osia.osia_erp.OsiaErpApplication>(*args) }
