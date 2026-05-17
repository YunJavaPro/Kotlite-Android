package com.sunnychung.gradle.plugin.kotlite.codegenerator

import com.sunnychung.gradle.plugin.kotlite.codegenerator.domain.StdLibDelegationCodeGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class KotliteCommonKotlinCodeGenerateTask : DefaultTask() {
    @get:InputDirectory
    abstract val inputDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:Input
    abstract val outputPackage: Property<String>

    @get:Input
    abstract val configs: MapProperty<String, KotliteModuleConfig>

    @TaskAction
    fun run() {
        try {
            generate()
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    fun generate() {
        val outputDirObj = outputDir.asFile.get()
        outputDirObj.mkdirs()

        val moduleNames = mutableListOf<String>()

        inputDir.asFileTree.forEach {
            if (!it.name.endsWith(".kt")) return@forEach
            val name = it.nameWithoutExtension
            moduleNames += name

            val config = configs.get()[name] ?: KotliteModuleConfig()
            val output = StdLibDelegationCodeGenerator(
                name = name,
                code = it.readText(),
                outputPackage = outputPackage.get(),
                config = config
            ).generate()
            File(outputDirObj, "Abstract${name}LibModule.kt").writeText(output)
            println("✓ Generated ${outputPackage.get()}.Abstract${name}LibModule")
        }

        // Generate concrete modules (XxxLibModule)
        moduleNames.forEach { name ->
            val content = """
/** Generated code. DO NOT MODIFY! Changes to this file will be overwritten. **/
package ${outputPackage.get()}

class ${name}LibModule : Abstract${name}LibModule()
            """.trimIndent()

            File(outputDirObj, "${name}LibModule.kt").writeText(content)
            println("✓ Generated ${outputPackage.get()}.${name}LibModule")
        }

        // Generate StdLibModuleRegistry
        val registryContent = """
/** Generated code. DO NOT MODIFY! Changes to this file will be overwritten. **/
package ${outputPackage.get()}

import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule

object StdLibModuleRegistry {
    
    /**
     * Get all available stdlib modules.
     * Note: IOLibModule is NOT included here because it may need special handling.
     */
    fun getAllNonIOModules(): List<LibraryModule> = listOf(
${moduleNames.filter { it != "IO" }.joinToString(",\n") { "        ${it}LibModule()" }}
    )
}
        """.trimIndent()

        File(outputDirObj, "StdLibModuleRegistry.kt").writeText(registryContent)
        println("✓ Generated ${outputPackage.get()}.StdLibModuleRegistry")
    }
}
