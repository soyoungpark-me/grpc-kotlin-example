package com.example.server

import com.example.server.infrastructure.api.CatalogGrpcService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ServerProperties::class)
class ServerApplication

fun main(args: Array<String>) {
    val ctx = runApplication<ServerApplication>(*args)
    val port = ctx.getBean(ServerProperties::class.java).port
    val server = CatalogGrpcService(port)
    server.start()
    server.blockUntilShutdown()
}

@ConfigurationProperties("grpc")
@ConstructorBinding
data class ServerProperties(
    val port: Int
)