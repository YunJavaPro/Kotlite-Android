plugins {
    `java-gradle-plugin`
    kotlin("jvm")
}

version = "1.0.0"

group = "io.github.sunny-chung"

gradlePlugin {
    plugins {
        create("stdlibProcessorPlugin") {
            id = "io.github.sunny-chung.kotlite-stdlib-processor-plugin"
            implementationClass = "com.sunnychung.gradle.plugin.kotlite.codegenerator.KotliteLibHeaderProcessorPlugin"
            displayName = "Kotlite Stdlib Processor"
            description = "Kotlite Stdlib Header Processor"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":kotlite-interpreter"))
    implementation(kotlin("stdlib-jdk8"))
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
