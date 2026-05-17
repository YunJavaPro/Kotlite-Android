package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*

class ByteLibModule : AbstractByteLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        ByteArrayClass.clazz,
    )
}
