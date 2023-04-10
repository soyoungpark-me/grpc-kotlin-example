plugins {
    java
}

dependencies {
    implementation("io.grpc:grpc-netty")
    implementation(project(":libs:domain"))
    implementation(project(":libs:grpc"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
//    implementation("io.zipkin.brave:brave-instrumentation-grpc")

    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}