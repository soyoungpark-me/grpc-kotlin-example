//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: catalog.proto

package com.example.server;

@kotlin.jvm.JvmName("-initializecatalogRequest")
inline fun catalogRequest(block: com.example.server.CatalogRequestKt.Dsl.() -> kotlin.Unit): com.example.server.CatalogRequest =
  com.example.server.CatalogRequestKt.Dsl._create(com.example.server.CatalogRequest.newBuilder()).apply { block() }._build()
object CatalogRequestKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    private val _builder: com.example.server.CatalogRequest.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: com.example.server.CatalogRequest.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): com.example.server.CatalogRequest = _builder.build()

    /**
     * <code>string name = 1;</code>
     */
    var name: kotlin.String
      @JvmName("getName")
      get() = _builder.getName()
      @JvmName("setName")
      set(value) {
        _builder.setName(value)
      }
    /**
     * <code>string name = 1;</code>
     */
    fun clearName() {
      _builder.clearName()
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun com.example.server.CatalogRequest.copy(block: com.example.server.CatalogRequestKt.Dsl.() -> kotlin.Unit): com.example.server.CatalogRequest =
  com.example.server.CatalogRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()

