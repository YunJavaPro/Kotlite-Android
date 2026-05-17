package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*
import com.sunnychung.lib.multiplatform.kotlite.stdlib.regex.RegexClass

class RegexLibModule : AbstractRegexLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        RegexClass.clazz,
    )
}
