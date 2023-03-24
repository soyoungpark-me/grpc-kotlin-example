pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

rootProject.name = "grpc-test"
include("libs")
include("libs:domain")
include("libs:grpc")
include("apps:server")
include("apps:client")