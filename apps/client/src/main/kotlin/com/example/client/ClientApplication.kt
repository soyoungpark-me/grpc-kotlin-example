package com.example.client

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableConfigurationProperties(ClientProperties::class)
@EnableFeignClients
class ClientApplication

fun main(args: Array<String>) {
    runApplication<ClientApplication>(*args)
}

@ConfigurationProperties("grpc")
@ConstructorBinding
data class ClientProperties(
    val host: String,
    val port: Int
)