import com.sunnychung.gradle.plugin.kotlite.codegenerator.KotliteModuleConfig
import com.sunnychung.lib.multiplatform.kotlite.stdlib.processor.GenerateLibModulesTask

plugins {
    kotlin("multiplatform")
    id("io.github.sunny-chung.kotlite-stdlib-processor-plugin") version "1.0.0"
}

kotlin {
    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/common")
            kotlin.srcDir("build/generated/concrete")
            dependencies {
                implementation("co.touchlab:kermit:1.0.0")
                implementation("io.github.sunny-chung:kdatetime-multiplatform:1.0.0")
                implementation("com.benasher44:uuid:0.8.4")
                implementation(project(":kotlite-interpreter")) 
            }
        }
    }
}

val generateLibModules = tasks.register<GenerateLibModulesTask>("generateLibModules") {
    abstractModuleDir.set(file("build/generated/common"))
    concreteModuleOutputDir.set(file("build/generated/concrete"))
    outputPackage.set("com.sunnychung.lib.multiplatform.kotlite.stdlib")
    
    dependsOn("kotliteStdlibHeaderProcess")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(generateLibModules)
}

kotliteStdLibHeaderProcessor {
    inputDir = "src/kotlinheader/"
    outputDir = "build/generated/common/"
    outputPackage = "com.sunnychung.lib.multiplatform.kotlite.stdlib"
    configs = mapOf(
        "Regex" to KotliteModuleConfig(
            imports = listOf(
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.regex.RegexClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.regex.RegexValue",
            )
        ),
        "KDateTime" to KotliteModuleConfig(
            imports = listOf(
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KInstantValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZonedInstantValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZoneOffsetValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDurationValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZonedDateTimeValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDateValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDateTimeFormatValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDateTimeFormattableInterface",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KPointOfTimeValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KFixedTimeUnitValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KInstantClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZonedInstantClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZoneOffsetClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDurationClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KZonedDateTimeClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDateClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KDateTimeFormatClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KPointOfTimeClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.kdatetime.KFixedTimeUnitClass",
                "com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit",
                "com.sunnychung.lib.multiplatform.kdatetime.KInstant",
                "com.sunnychung.lib.multiplatform.kdatetime.KZonedInstant",
                "com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset",
                "com.sunnychung.lib.multiplatform.kdatetime.KDuration",
                "com.sunnychung.lib.multiplatform.kdatetime.KZonedDateTime",
                "com.sunnychung.lib.multiplatform.kdatetime.KDate",
                "com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormat",
                "com.sunnychung.lib.multiplatform.kdatetime.KDateTimeFormattable",
                "com.sunnychung.lib.multiplatform.kdatetime.KPointOfTime",
                "com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.milliseconds",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.seconds",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.minutes",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.hours",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.days",
                "com.sunnychung.lib.multiplatform.kdatetime.extension.weeks",
            )
        ),
        "Collections" to KotliteModuleConfig(
            imports = listOf(
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableListValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableMapValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapEntryIterator",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapEntryValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.SetValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableSetValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.wrap",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableListClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableMapClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapEntryClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.SetClass",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableSetClass",
            ),
            typeAliases = mapOf(
                "MapEntry<K, V>" to "Map.Entry<K, V>"
            ),
        ),
        "Core" to KotliteModuleConfig(),
        "Math" to KotliteModuleConfig(
            imports = listOf("kotlin.math.*")
        ),
        "Byte" to KotliteModuleConfig(
            imports = listOf(
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.byte.ByteArrayValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.byte.wrap",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.SetValue",
            )
        ),
        "Range" to KotliteModuleConfig(
            imports = listOf(
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.ClosedRangeValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.OpenEndRangeValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.IntProgressionValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.IntRangeValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.LongProgressionValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.range.LongRangeValue",
            )
        ),
        "Uuid" to KotliteModuleConfig(
            imports = listOf(
                "com.benasher44.uuid.Uuid",
                "com.benasher44.uuid.bytes",
                "com.benasher44.uuid.variant",
                "com.benasher44.uuid.version",
                "com.benasher44.uuid.uuidFrom",
                "com.benasher44.uuid.uuidOf",
                "com.benasher44.uuid.uuid4",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.byte.ByteArrayValue",
                "com.sunnychung.lib.multiplatform.kotlite.stdlib.uuid.UuidValue",
            )
        ),
    )
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}