plugins {
    java
}

dependencies {
    implementation(project(":libs:domain"))
    implementation(project(":libs:grpc"))
    implementation("io.grpc:grpc-okhttp")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.openfeign:feign-httpclient")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}