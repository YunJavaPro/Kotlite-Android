package com.sunnychung.lib.multiplatform.kotlite.stdlib.processor

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateLibModulesTask : DefaultTask() {

    @get:InputDirectory
    abstract val abstractModuleDir: DirectoryProperty

    @get:OutputDirectory
    abstract val concreteModuleOutputDir: DirectoryProperty

    @get:Input
    abstract val outputPackage: Property<String>

    @TaskAction
    fun generate() {
        val abstractDir = abstractModuleDir.asFile.get()
        val concreteDir = concreteModuleOutputDir.asFile.get()

        concreteDir.mkdirs()

        // Find all AbstractXxxLibModule.kt files
        val abstractFiles = abstractDir.listFiles()
            ?.filter { it.name.startsWith("Abstract") && it.name.endsWith("LibModule.kt") }
            ?: emptyList()

        // Generate corresponding XxxLibModule.kt files
        abstractFiles.forEach { abstractFile ->
            val abstractName = abstractFile.nameWithoutExtension // "AbstractTextLibModule"
            val concreteName = abstractName.removePrefix("Abstract") // "TextLibModule"
            val moduleName = concreteName.removeSuffix("LibModule") // "Text"

            val content = """
/** Generated code. DO NOT MODIFY! Changes to this file will be overwritten. **/
package ${outputPackage.get()}

class ${concreteName} : ${abstractName}()
            """.trimIndent()

            File(concreteDir, "$concreteName.kt").writeText(content)
            println("✓ Generated ${outputPackage.get()}.$concreteName")
        }

        // Also generate a registry file that can automatically list all modules
        val moduleNames = abstractFiles.map { 
            it.nameWithoutExtension.removePrefix("Abstract").removeSuffix("LibModule")
        }
        
        val registryContent = """
/** Generated code. DO NOT MODIFY! Changes to this file will be overwritten. **/
package ${outputPackage.get()}

import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule

/**
 * Automatically generated registry of all stdlib modules.
 */
object StdLibModuleRegistry {
    
    /**
     * Get all available stdlib modules except IOLibModule.
     * Note: IOLibModule needs special handling for output function.
     */
    fun getAllModules(
        outputToConsole: (String) -> Unit = { print(it) }
    ): List<LibraryModule> = listOfNotNull(
${moduleNames.filter { it != "IO" }.joinToString(",\n") { "        ${it}LibModule()" }},
        object : IOLibModule() {
            override fun outputToConsole(output: String) {
                outputToConsole(output)
            }
        }
    )
}
        """.trimIndent()

        File(concreteDir, "StdLibModuleRegistry.kt").writeText(registryContent)
        println("✓ Generated ${outputPackage.get()}.StdLibModuleRegistry")
    }
}
