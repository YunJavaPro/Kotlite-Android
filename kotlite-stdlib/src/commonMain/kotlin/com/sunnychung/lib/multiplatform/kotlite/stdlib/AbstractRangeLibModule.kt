package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*
import com.sunnychung.lib.multiplatform.kotlite.stdlib.range.*

abstract class AbstractRangeLibModule : LibraryModule("Range") {
    override val classes: List<ProvidedClassDefinition> = emptyList()

    override val properties: List<ExtensionProperty> = listOf(
        ExtensionProperty(
            declaredName = "start",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "ClosedRange<T>",
            type = "T",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as ClosedRange<*>
                val st = interpreter.symbolTable()
                when (val element = value.start) {
                    is Int -> IntValue(element, st)
                    is Long -> LongValue(element, st)
                    is Byte -> ByteValue(element, st)
                    is Short -> IntValue(element.toInt(), st)
                    is Double -> DoubleValue(element, st)
                    is Float -> DoubleValue(element.toDouble(), st)
                    else -> DelegatedValue(element, typeArgs["T"]!!.name, typeArguments = emptyList(), symbolTable = st)
                }
            },
        ),
        ExtensionProperty(
            declaredName = "endInclusive",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "ClosedRange<T>",
            type = "T",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as ClosedRange<*>
                val st = interpreter.symbolTable()
                when (val element = value.endInclusive) {
                    is Int -> IntValue(element, st)
                    is Long -> LongValue(element, st)
                    is Byte -> ByteValue(element, st)
                    is Short -> IntValue(element.toInt(), st)
                    is Double -> DoubleValue(element, st)
                    is Float -> DoubleValue(element.toDouble(), st)
                    else -> DelegatedValue(element, typeArgs["T"]!!.name, typeArguments = emptyList(), symbolTable = st)
                }
            },
        ),
        ExtensionProperty(
            declaredName = "isEmpty",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "ClosedRange<T>",
            type = "Boolean",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as ClosedRange<*>
                BooleanValue(value.isEmpty(), interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "start",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "OpenEndRange<T>",
            type = "T",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as OpenEndRange<*>
                val st = interpreter.symbolTable()
                when (val element = value.start) {
                    is Int -> IntValue(element, st)
                    is Long -> LongValue(element, st)
                    is Byte -> ByteValue(element, st)
                    is Short -> IntValue(element.toInt(), st)
                    is Double -> DoubleValue(element, st)
                    is Float -> DoubleValue(element.toDouble(), st)
                    else -> DelegatedValue(element, typeArgs["T"]!!.name, typeArguments = emptyList(), symbolTable = st)
                }
            },
        ),
        ExtensionProperty(
            declaredName = "endExclusive",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "OpenEndRange<T>",
            type = "T",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as OpenEndRange<*>
                val st = interpreter.symbolTable()
                when (val element = value.endExclusive) {
                    is Int -> IntValue(element, st)
                    is Long -> LongValue(element, st)
                    is Byte -> ByteValue(element, st)
                    is Short -> IntValue(element.toInt(), st)
                    is Double -> DoubleValue(element, st)
                    is Float -> DoubleValue(element.toDouble(), st)
                    else -> DelegatedValue(element, typeArgs["T"]!!.name, typeArguments = emptyList(), symbolTable = st)
                }
            },
        ),
        ExtensionProperty(
            declaredName = "isEmpty",
            typeParameters = listOf(TypeParameter("T", "Comparable<T>")),
            receiver = "OpenEndRange<T>",
            type = "Boolean",
            getter = { interpreter, receiver, typeArgs ->
                val value = (receiver as DelegatedValue<*>).value as OpenEndRange<*>
                BooleanValue(value.isEmpty(), interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "first",
            receiver = "IntProgression",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as IntProgression
                IntValue(value.first, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "last",
            receiver = "IntProgression",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as IntProgression
                IntValue(value.last, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "step",
            receiver = "IntProgression",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as IntProgression
                IntValue(value.step, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "first",
            receiver = "LongProgression",
            type = "Long",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as LongProgression
                LongValue(value.first, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "last",
            receiver = "LongProgression",
            type = "Long",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as LongProgression
                LongValue(value.last, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "step",
            receiver = "LongProgression",
            type = "Long",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as LongProgression
                LongValue(value.step, interpreter.symbolTable())
            },
        ),
    )

    override val globalProperties: List<GlobalProperty> = emptyList()

    override val functions: List<CustomFunctionDefinition> = listOf(
        // operator fun Int.rangeTo(that: Int): IntRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 5, 1),
            receiverType = "Int",
            functionName = "rangeTo",
            returnType = "IntRange",
            parameterTypes = listOf(CustomFunctionParameter("that", "Int")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val that = (args[0] as IntValue).value
                IntRangeValue(r..that, st)
            },
        ),
        // operator fun Int.rangeUntil(that: Int): IntRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 6, 1),
            receiverType = "Int",
            functionName = "rangeUntil",
            returnType = "IntRange",
            parameterTypes = listOf(CustomFunctionParameter("that", "Int")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val that = (args[0] as IntValue).value
                IntRangeValue(r until that, st)
            },
        ),
        // operator fun Long.rangeTo(that: Long): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 7, 1),
            receiverType = "Long",
            functionName = "rangeTo",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("that", "Long")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val that = (args[0] as LongValue).value
                LongRangeValue(r..that, st)
            },
        ),
        // operator fun Long.rangeUntil(that: Long): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 8, 1),
            receiverType = "Long",
            functionName = "rangeUntil",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("that", "Long")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val that = (args[0] as LongValue).value
                LongRangeValue(r until that, st)
            },
        ),
        // operator fun IntRange.contains(element: Int?): Boolean
        CustomFunctionDefinition(
            position = SourcePosition("Range", 9, 1),
            receiverType = "IntRange",
            functionName = "contains",
            returnType = "Boolean",
            parameterTypes = listOf(CustomFunctionParameter("element", "Int?")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val range = (receiver as DelegatedValue<*>).value as IntRange
                val element = if (args[0] is IntValue) (args[0] as IntValue).value else null
                BooleanValue(range.contains(element), interpreter.symbolTable())
            },
        ),
        // operator fun LongRange.contains(element: Long?): Boolean
        CustomFunctionDefinition(
            position = SourcePosition("Range", 10, 1),
            receiverType = "LongRange",
            functionName = "contains",
            returnType = "Boolean",
            parameterTypes = listOf(CustomFunctionParameter("element", "Long?")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { interpreter, receiver, args, _ ->
                val range = (receiver as DelegatedValue<*>).value as LongRange
                val element = if (args[0] is LongValue) (args[0] as LongValue).value else null
                BooleanValue(range.contains(element), interpreter.symbolTable())
            },
        ),
        // infix fun Int.downTo(to: Byte): IntProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 11, 1),
            receiverType = "Int",
            functionName = "downTo",
            returnType = "IntProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Byte")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as ByteValue).value
                IntProgressionValue(r downTo to.toInt(), st)
            },
        ),
        // infix fun Int.downTo(to: Int): IntProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 12, 1),
            receiverType = "Int",
            functionName = "downTo",
            returnType = "IntProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Int")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as IntValue).value
                IntProgressionValue(r downTo to, st)
            },
        ),
        // infix fun Int.downTo(to: Long): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 13, 1),
            receiverType = "Int",
            functionName = "downTo",
            returnType = "LongProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Long")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as LongValue).value
                LongProgressionValue(r.toLong() downTo to, st)
            },
        ),
        // infix fun Int.until(to: Byte): IntRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 14, 1),
            receiverType = "Int",
            functionName = "until",
            returnType = "IntRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Byte")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as ByteValue).value
                IntRangeValue(r until to.toInt(), st)
            },
        ),
        // infix fun Int.until(to: Int): IntRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 15, 1),
            receiverType = "Int",
            functionName = "until",
            returnType = "IntRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Int")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as IntValue).value
                IntRangeValue(r until to, st)
            },
        ),
        // infix fun Int.until(to: Long): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 16, 1),
            receiverType = "Int",
            functionName = "until",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Long")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as IntValue).value
                val to = (args[0] as LongValue).value
                LongRangeValue(r.toLong() until to, st)
            },
        ),
        // infix fun Long.downTo(to: Byte): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 17, 1),
            receiverType = "Long",
            functionName = "downTo",
            returnType = "LongProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Byte")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as ByteValue).value
                LongProgressionValue(r downTo to.toLong(), st)
            },
        ),
        // infix fun Long.downTo(to: Int): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 18, 1),
            receiverType = "Long",
            functionName = "downTo",
            returnType = "LongProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Int")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as IntValue).value
                LongProgressionValue(r downTo to.toLong(), st)
            },
        ),
        // infix fun Long.downTo(to: Long): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 19, 1),
            receiverType = "Long",
            functionName = "downTo",
            returnType = "LongProgression",
            parameterTypes = listOf(CustomFunctionParameter("to", "Long")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as LongValue).value
                LongProgressionValue(r downTo to, st)
            },
        ),
        // infix fun Long.until(to: Byte): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 20, 1),
            receiverType = "Long",
            functionName = "until",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Byte")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as ByteValue).value
                LongRangeValue(r until to.toLong(), st)
            },
        ),
        // infix fun Long.until(to: Int): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 21, 1),
            receiverType = "Long",
            functionName = "until",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Int")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as IntValue).value
                LongRangeValue(r until to.toLong(), st)
            },
        ),
        // infix fun Long.until(to: Long): LongRange
        CustomFunctionDefinition(
            position = SourcePosition("Range", 22, 1),
            receiverType = "Long",
            functionName = "until",
            returnType = "LongRange",
            parameterTypes = listOf(CustomFunctionParameter("to", "Long")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val r = (receiver as LongValue).value
                val to = (args[0] as LongValue).value
                LongRangeValue(r until to, st)
            },
        ),
        // infix fun IntProgression.step(step: Int): IntProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 23, 1),
            receiverType = "IntProgression",
            functionName = "step",
            returnType = "IntProgression",
            parameterTypes = listOf(CustomFunctionParameter("step", "Int")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val progression = (receiver as DelegatedValue<*>).value as IntProgression
                val step = (args[0] as IntValue).value
                IntProgressionValue(progression step step, st)
            },
        ),
        // infix fun LongProgression.step(step: Long): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 24, 1),
            receiverType = "LongProgression",
            functionName = "step",
            returnType = "LongProgression",
            parameterTypes = listOf(CustomFunctionParameter("step", "Long")),
            modifiers = setOf(FunctionModifier.infix),
            executable = { interpreter, receiver, args, _ ->
                val st = interpreter.symbolTable()
                val progression = (receiver as DelegatedValue<*>).value as LongProgression
                val step = (args[0] as LongValue).value
                LongProgressionValue(progression step step, st)
            },
        ),
        // fun IntProgression.isEmpty(): Boolean
        CustomFunctionDefinition(
            position = SourcePosition("Range", 25, 1),
            receiverType = "IntProgression",
            functionName = "isEmpty",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val progression = (receiver as DelegatedValue<*>).value as IntProgression
                BooleanValue(progression.isEmpty(), interpreter.symbolTable())
            },
        ),
        // fun IntProgression.reversed(): IntProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 26, 1),
            receiverType = "IntProgression",
            functionName = "reversed",
            returnType = "IntProgression",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val st = interpreter.symbolTable()
                val progression = (receiver as DelegatedValue<*>).value as IntProgression
                IntProgressionValue(progression.reversed(), st)
            },
        ),
        // fun LongProgression.isEmpty(): Boolean
        CustomFunctionDefinition(
            position = SourcePosition("Range", 27, 1),
            receiverType = "LongProgression",
            functionName = "isEmpty",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val progression = (receiver as DelegatedValue<*>).value as LongProgression
                BooleanValue(progression.isEmpty(), interpreter.symbolTable())
            },
        ),
        // fun LongProgression.reversed(): LongProgression
        CustomFunctionDefinition(
            position = SourcePosition("Range", 28, 1),
            receiverType = "LongProgression",
            functionName = "reversed",
            returnType = "LongProgression",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val st = interpreter.symbolTable()
                val progression = (receiver as DelegatedValue<*>).value as LongProgression
                LongProgressionValue(progression.reversed(), st)
            },
        ),
        // fun IntRange.random(): Int
        CustomFunctionDefinition(
            position = SourcePosition("Range", 29, 1),
            receiverType = "IntRange",
            functionName = "random",
            returnType = "Int",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val range = (receiver as DelegatedValue<*>).value as IntRange
                IntValue(range.random(), interpreter.symbolTable())
            },
        ),
        // fun IntRange.randomOrNull(): Int?
        CustomFunctionDefinition(
            position = SourcePosition("Range", 30, 1),
            receiverType = "IntRange",
            functionName = "randomOrNull",
            returnType = "Int?",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val range = (receiver as DelegatedValue<*>).value as IntRange
                val result = range.randomOrNull()
                if (result != null) IntValue(result, interpreter.symbolTable()) else NullValue
            },
        ),
        // fun LongRange.random(): Long
        CustomFunctionDefinition(
            position = SourcePosition("Range", 31, 1),
            receiverType = "LongRange",
            functionName = "random",
            returnType = "Long",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val range = (receiver as DelegatedValue<*>).value as LongRange
                LongValue(range.random(), interpreter.symbolTable())
            },
        ),
        // fun LongRange.randomOrNull(): Long?
        CustomFunctionDefinition(
            position = SourcePosition("Range", 32, 1),
            receiverType = "LongRange",
            functionName = "randomOrNull",
            returnType = "Long?",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val range = (receiver as DelegatedValue<*>).value as LongRange
                val result = range.randomOrNull()
                if (result != null) LongValue(result, interpreter.symbolTable()) else NullValue
            },
        ),
    )
}
