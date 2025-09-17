plugins {
	id("chirp.spring-boot-app")
}

group = "com.juanjoseabuin"
version = "0.0.1-SNAPSHOT"
description = "Chirp Backend"

dependencies {
    implementation(projects.chat)
    implementation(projects.notification)
    implementation(projects.user)
    implementation(projects.common)

    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
}
