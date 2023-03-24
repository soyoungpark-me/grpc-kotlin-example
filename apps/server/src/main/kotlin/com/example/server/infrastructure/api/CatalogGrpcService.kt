package com.example.server.infrastructure.api

import com.example.CatalogReply
import com.example.CatalogRequest
import com.example.CatalogServiceGrpcKt
import com.example.catalogReply
import com.example.server.infrastructure.persistence.CatalogRepository
import io.grpc.Server
import io.grpc.ServerBuilder

class CatalogGrpcService(private val port: Int) {
    val server: Server = ServerBuilder
        .forPort(port)
        .addService(CatalogService())
        .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@CatalogGrpcService.stop()
                println("*** server shut down")
            }
        )
    }

    private fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class CatalogService(
        private val catalogRepository: CatalogRepository = CatalogRepository()
    ) : CatalogServiceGrpcKt.CatalogServiceCoroutineImplBase() {
        override suspend fun catalog(request: CatalogRequest): CatalogReply {
            val response = catalogRepository.catalogs()

            return catalogReply {
                total = response.size.toLong()
                response.map {
                    this.catalogs.add(
                        com.example.catalog {
                            id = it.id!!
                            name = it.name!!
                        }
                    )
                }
            }
        }
    }
}