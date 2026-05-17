package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*
import com.sunnychung.lib.multiplatform.kotlite.stdlib.range.*

class RangeLibModule : AbstractRangeLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        IntProgressionClass.clazz,
        IntRangeClass.clazz,
        LongProgressionClass.clazz,
        LongRangeClass.clazz,
        ClosedRangeClass.clazz,
        OpenEndRangeClass.clazz,
    )
}
