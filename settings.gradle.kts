pluginManagement {
    includeBuild("build-logic")
	repositories {
		maven { url = uri("https://repo.spring.io/snapshot") }
        maven { url = uri("https://repo.spring.io/milestone") }
		gradlePluginPortal()
	}
}

rootProject.name = "chirp"

include("app")
include("user")
include("chat")
include("notification")
include("common")