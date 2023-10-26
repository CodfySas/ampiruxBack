package com.osia.nota_maestro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableFeignClients
@EnableRetry

class NotaMaestroApplication

fun main(args: Array<String>) { runApplication<com.osia.nota_maestro.NotaMaestroApplication>(*args) }
