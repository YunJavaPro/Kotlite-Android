package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*
import com.sunnychung.lib.multiplatform.kotlite.stdlib.byte.ByteArrayClass

class ByteLibModule : AbstractByteLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        ByteArrayClass.clazz,
    )
}
