package com.example.server.application

import org.springframework.cloud.sleuth.Tracer
import org.springframework.stereotype.Service

@Service
class AuditService(
    private val tracer: Tracer
) {
    companion object {
        const val idBaggageFieldName = "X-USER-ID"
    }

    fun getAuditId(): String? {
        return getTraceBaggageValue(idBaggageFieldName)
    }

    private fun getTraceBaggageValue(baggageName: String): String? {
        return tracer.getBaggage(baggageName)?.get()
    }
}