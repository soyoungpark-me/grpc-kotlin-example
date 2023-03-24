plugins {
    java
}

dependencies {
    implementation("io.grpc:grpc-netty")
    implementation(project(":libs:domain"))
    implementation(project(":libs:grpc"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}