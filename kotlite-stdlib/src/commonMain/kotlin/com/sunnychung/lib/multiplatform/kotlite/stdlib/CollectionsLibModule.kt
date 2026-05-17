package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.CollectionInterface
import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter
import com.sunnychung.lib.multiplatform.kotlite.model.DelegatedValue
import com.sunnychung.lib.multiplatform.kotlite.model.ExtensionProperty
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionModifier
import com.sunnychung.lib.multiplatform.kotlite.model.IntValue
import com.sunnychung.lib.multiplatform.kotlite.model.LambdaValue
import com.sunnychung.lib.multiplatform.kotlite.model.NullValue
import com.sunnychung.lib.multiplatform.kotlite.model.ProvidedClassDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.RuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.SourcePosition
import com.sunnychung.lib.multiplatform.kotlite.model.StringValue
import com.sunnychung.lib.multiplatform.kotlite.model.TypeParameter
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapClass
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MapEntryClass
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableListClass
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableMapClass
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.MutableSetClass
import com.sunnychung.lib.multiplatform.kotlite.stdlib.collections.SetClass

class CollectionsLibModule : AbstractCollectionsLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
        // they are now part of the interpreter core
//        IterableInterface.clazz,
//        CollectionInterface.collectionClazz,
//        ListValue.clazz,

        CollectionInterface.mutableCollectionClazz,
        MutableListClass.clazz,
        MapClass.clazz,
        MutableMapClass.clazz,
        MapEntryClass.clazz,
        SetClass.clazz,
        MutableSetClass.clazz,
    )

    override val properties: List<ExtensionProperty> = super.properties + listOf(
        ExtensionProperty(
            declaredName = "size",
            typeParameters = listOf(TypeParameter("T", null)),
            receiver = "List<T>",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                val value = (receiver as DelegatedValue<*>).value as List<*>
                IntValue(value.size, interpreter.symbolTable())
            },
        ),
    )

    override val functions: List<CustomFunctionDefinition> = super.functions + listOf(
        CustomFunctionDefinition(
            position = SourcePosition("Collections", 1, 1),
            receiverType = null,
            functionName = "listOf",
            returnType = "List<T>",
            typeParameters = listOf(TypeParameter(name = "T", typeUpperBound = null)),
            parameterTypes = listOf(CustomFunctionParameter(name = "elements", type = "T", modifiers = setOf("vararg"))),
            executable = { _, _, args, _ ->
                args[0]
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition("Collections", 1, 1),
            receiverType = "List<T>",
            functionName = "get",
            returnType = "T",
            typeParameters = listOf(TypeParameter(name = "T", typeUpperBound = null)),
            parameterTypes = listOf(CustomFunctionParameter(name = "index", type = "Int")),
            modifiers = setOf(FunctionModifier.operator),
            executable = { _, receiver, args, _ ->
                val list = (receiver as DelegatedValue<*>).value as List<RuntimeValue>
                list[(args[0] as IntValue).value]
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition("Collections", 1, 1),
            receiverType = "Iterable<T>",
            functionName = "joinToString",
            returnType = "String",
            typeParameters = listOf(TypeParameter(name = "T", typeUpperBound = null)),
            parameterTypes = listOf(
                CustomFunctionParameter(name = "separator", type = "String", defaultValueExpression = "\", \""),
                CustomFunctionParameter(name = "prefix", type = "String", defaultValueExpression = "\"\""),
                CustomFunctionParameter(name = "postfix", type = "String", defaultValueExpression = "\"\""),
                CustomFunctionParameter(name = "limit", type = "Int", defaultValueExpression = "-1"),
                CustomFunctionParameter(name = "truncated", type = "String", defaultValueExpression = "\"...\""),
                CustomFunctionParameter(name = "transform", type = "((T) -> String)?", defaultValueExpression = "null"),
            ),
            executable = { interpreter, receiver, args, typeArgs ->
                val symbolTable = interpreter.symbolTable()
                val iterable = (receiver as DelegatedValue<*>).value as Iterable<RuntimeValue>
                val separator = (args[0] as StringValue).value
                val prefix = (args[1] as StringValue).value
                val postfix = (args[2] as StringValue).value
                val limit = (args[3] as IntValue).value
                val truncated = (args[4] as StringValue).value
                val transform = args[5].let { if (it == NullValue) null else it as LambdaValue }

                val sb = StringBuilder(prefix)
                var index = 0
                for (e in iterable) {
                    if (limit >= 0 && index >= limit) {
                        sb.append(truncated)
                        break
                    }
                    if (index > 0) sb.append(separator)
                    val part = transform?.execute(arrayOf(e))?.convertToString() ?: e.convertToString()
                    sb.append(part)
                    index++
                }
                sb.append(postfix)
                StringValue(sb.toString(), symbolTable)
            },
        ),
    )
}