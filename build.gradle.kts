import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.example"
version = "0.0.1-SNAPSHOT"

ext["grpcVersion"] = "1.47.0"
ext["grpcKotlinVersion"] = "1.3.0" // CURRENT_GRPC_KOTLIN_VERSION
ext["protobufVersion"] = "3.21.2"
ext["coroutinesVersion"] = "1.6.2"

plugins {
	val kotlinVersion = "1.6.21"
	java
	application
	idea
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("kapt") version kotlinVersion
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	id("org.springframework.boot") version "2.6.8"
	id("com.google.protobuf") version "0.8.18"
}

allprojects {
	repositories {
		mavenCentral()
		maven(url = "https://plugins.gradle.org/m2/")
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "idea")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "java-library")
	apply(plugin = "kotlin-spring")
	apply(plugin = "kotlin-kapt")
	apply(plugin = "com.google.protobuf")

	dependencyManagement {
		dependencies {
			val grpcVersion = "1.47.0"
			dependency("io.grpc:grpc-okhttp:$grpcVersion")
			dependency("io.grpc:grpc-netty:$grpcVersion")
			dependency("io.grpc:grpc-protobuf:$grpcVersion")
			dependency("io.grpc:grpc-stub:$grpcVersion")

			val grpcKotlinVersion = "1.3.0"
			dependency("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")

			val protobufVersion = "3.21.2"
			dependency("com.google.protobuf:protobuf-java-util:$protobufVersion")
			dependency("com.google.protobuf:protobuf-kotlin:$protobufVersion")

			val coroutinesVersion = "1.6.2"
			dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
			dependency("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")

			val feignVersion = "11.10"
			dependency("io.github.openfeign:feign-okhttp:$feignVersion")
			dependency("io.github.openfeign:feign-httpclient:$feignVersion")
		}
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
			mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
		}
	}
	dependencies {
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
		implementation("net.devh:grpc-spring-boot-starter:2.14.0.RELEASE")
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

project(":libs") {
	subprojects {
		tasks.jar {
			enabled = true
		}
	}
}

project(":apps") {
	subprojects {
		apply(plugin = "org.springframework.boot")
		apply(plugin = "application")
		dependencies {
			implementation("org.springframework.boot:spring-boot-starter")
		}
	}
}