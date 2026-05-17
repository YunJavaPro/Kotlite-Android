package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*

class RegexLibModule : AbstractRegexLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        RegexClass.clazz,
    )
}
