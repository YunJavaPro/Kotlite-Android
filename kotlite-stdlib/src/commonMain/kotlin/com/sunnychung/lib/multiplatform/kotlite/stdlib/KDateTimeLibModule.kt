package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*

class KDateTimeLibModule : AbstractKDateTimeLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        KInstantClass.clazz,
        KZonedInstantClass.clazz,
        KZoneOffsetClass.clazz,
        KDurationClass.clazz,
        KZonedDateTimeClass.clazz,
        KDateClass.clazz,
        KDateTimeFormatClass.clazz,
        KPointOfTimeClass.clazz,
        KFixedTimeUnitClass.clazz,
        KDateTimeFormattableInterface.clazz,
    )
}
