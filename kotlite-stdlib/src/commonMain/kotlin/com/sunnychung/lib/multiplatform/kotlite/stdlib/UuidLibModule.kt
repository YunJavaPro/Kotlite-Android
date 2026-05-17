package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*

class UuidLibModule : AbstractUuidLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        UuidClass.clazz,
    )
}
