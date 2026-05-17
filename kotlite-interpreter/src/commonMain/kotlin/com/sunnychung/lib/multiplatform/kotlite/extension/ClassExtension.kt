
package com.sunnychung.lib.multiplatform.kotlite.extension

// 添加通用实现（这在 JVM 和 Android 上都能工作）
val Any.fullClassName: String
    get() = this::class.qualifiedName ?: this::class.simpleName ?: "<anonymous>"