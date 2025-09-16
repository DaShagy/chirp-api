pluginManagement {
	repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "chirp"

include("app")
include("user")
include("chat")
include("notification")
include("common")