package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.ExtensionProperty
import com.sunnychung.lib.multiplatform.kotlite.model.GlobalProperty
import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule
import com.sunnychung.lib.multiplatform.kotlite.model.ProvidedClassDefinition

abstract class AbstractMathLibModule : LibraryModule("Math") {
    override val classes: List<ProvidedClassDefinition> = emptyList()
    override val properties: List<ExtensionProperty> = emptyList()
    override val globalProperties: List<GlobalProperty> = emptyList()
    override val functions: List<CustomFunctionDefinition> = emptyList()
}
