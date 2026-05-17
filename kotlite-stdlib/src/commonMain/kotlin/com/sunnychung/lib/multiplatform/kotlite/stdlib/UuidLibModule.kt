package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*
import com.sunnychung.lib.multiplatform.kotlite.stdlib.uuid.UuidClass

class UuidLibModule : AbstractUuidLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        UuidClass.clazz,
    )
}
