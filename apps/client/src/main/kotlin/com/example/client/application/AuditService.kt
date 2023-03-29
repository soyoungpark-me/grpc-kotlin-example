package com.example.client.application

import brave.baggage.BaggageField
import org.springframework.cloud.sleuth.Tracer
import org.springframework.stereotype.Service

@Service
class AuditService(
    private val tracer: Tracer
) {
    companion object {
        const val idBaggageFieldName = "X-USER-ID"
    }

    var id: String?
        set(value) {
            BaggageField.create(idBaggageFieldName).updateValue(value)
        }
        get() {
            return getTraceBaggageValue(idBaggageFieldName)
        }

    private fun getTraceBaggageValue(baggageName: String): String? {
        return tracer.getBaggage(baggageName)?.get()
    }
}