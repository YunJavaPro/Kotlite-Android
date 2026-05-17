package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.*

class CollectionsLibModule : AbstractCollectionsLibModule() {
    override val classes: List<ProvidedClassDefinition> = listOf(
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
    )
}
