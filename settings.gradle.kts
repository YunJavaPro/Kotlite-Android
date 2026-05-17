pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://mirrors.tuna.tsinghua.edu.cn/maven2")
        maven("https://jitpack.io")
    }
}

rootProject.name = "KotliteDemo"

include(":kotlite-interpreter")
include(":kotlite-stdlib")
include(":kotlite-stdlib-processor-gradle-plugin")
include(":app")