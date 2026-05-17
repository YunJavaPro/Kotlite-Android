package com.sunnychung.gradle.plugin.kotlite.codegenerator.domain

import com.sunnychung.gradle.plugin.kotlite.codegenerator.KotliteModuleConfig
import com.sunnychung.lib.multiplatform.kotlite.CodeGenerator
import com.sunnychung.lib.multiplatform.kotlite.Parser
import com.sunnychung.lib.multiplatform.kotlite.extension.resolveGenericParameterTypeToUpperBound
import com.sunnychung.lib.multiplatform.kotlite.lexer.Lexer
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionDeclarationNode
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionModifier
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionTypeNode
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionValueParameterModifier
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionValueParameterNode
import com.sunnychung.lib.multiplatform.kotlite.model.PropertyDeclarationNode
import com.sunnychung.lib.multiplatform.kotlite.model.SourcePosition
import com.sunnychung.lib.multiplatform.kotlite.model.TypeNode
import com.sunnychung.lib.multiplatform.kotlite.model.TypeParameterNode
import com.sunnychung.lib.multiplatform.kotlite.model.isPrimitive
import com.sunnychung.lib.multiplatform.kotlite.model.isPrimitiveWithValue

internal val isDebug: Boolean = false

internal class StdLibDelegationCodeGenerator(val name: String, val code: String, val outputPackage: String, val config: KotliteModuleConfig) {
    val parser = Parser(Lexer(name, code))
    val extensionProperties: List<PropertyDeclarationNode>
    val functionInterfaces: List<FunctionDeclarationNode>
    init {
        val parsed = parser.libHeaderFile()
        extensionProperties = parsed.filterIsInstance<PropertyDeclarationNode>()
        functionInterfaces = parsed.filterIsInstance<FunctionDeclarationNode>()
    }
    init {
        if (name.any { !it.isJavaIdentifierPart() }) {
            throw IllegalArgumentException("Provided name should be a valid String that can be included as a part of a Kotlin identifier")
        }
    }
    fun generate(): String {
        return """
/** Generated code. DO NOT MODIFY! Changes to this file will be overwritten. **/
package $outputPackage
import com.sunnychung.lib.multiplatform.kotlite.model.AnyType
import com.sunnychung.lib.multiplatform.kotlite.model.BooleanValue
import com.sunnychung.lib.multiplatform.kotlite.model.ByteValue
import com.sunnychung.lib.multiplatform.kotlite.model.ComparableRuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter
import com.sunnychung.lib.multiplatform.kotlite.model.CharValue
import com.sunnychung.lib.multiplatform.kotlite.model.DataType
import com.sunnychung.lib.multiplatform.kotlite.model.DelegatedValue
import com.sunnychung.lib.multiplatform.kotlite.model.DoubleValue
import com.sunnychung.lib.multiplatform.kotlite.model.ExtensionProperty
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionModifier
import com.sunnychung.lib.multiplatform.kotlite.model.FunctionType
import com.sunnychung.lib.multiplatform.kotlite.model.GlobalProperty
import com.sunnychung.lib.multiplatform.kotlite.model.IntValue
import com.sunnychung.lib.multiplatform.kotlite.model.IterableValue
import com.sunnychung.lib.multiplatform.kotlite.model.IteratorClass
import com.sunnychung.lib.multiplatform.kotlite.model.IteratorValue
import com.sunnychung.lib.multiplatform.kotlite.model.KotlinValueHolder
import com.sunnychung.lib.multiplatform.kotlite.model.LambdaValue
import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule
import com.sunnychung.lib.multiplatform.kotlite.model.ListClass
import com.sunnychung.lib.multiplatform.kotlite.model.ListValue
import com.sunnychung.lib.multiplatform.kotlite.model.LongValue
import com.sunnychung.lib.multiplatform.kotlite.model.NothingType
import com.sunnychung.lib.multiplatform.kotlite.model.NullValue
import com.sunnychung.lib.multiplatform.kotlite.model.ObjectType
import com.sunnychung.lib.multiplatform.kotlite.model.PairClass
import com.sunnychung.lib.multiplatform.kotlite.model.PairValue
import com.sunnychung.lib.multiplatform.kotlite.model.PrimitiveIterableValue
import com.sunnychung.lib.multiplatform.kotlite.model.PrimitiveIteratorValue
import com.sunnychung.lib.multiplatform.kotlite.model.PrimitiveType
import com.sunnychung.lib.multiplatform.kotlite.model.PrimitiveTypeName
import com.sunnychung.lib.multiplatform.kotlite.model.ProvidedClassDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.SourcePosition
import com.sunnychung.lib.multiplatform.kotlite.model.StringValue
import com.sunnychung.lib.multiplatform.kotlite.model.TypeParameter
import com.sunnychung.lib.multiplatform.kotlite.model.TypeParameterType
import com.sunnychung.lib.multiplatform.kotlite.model.RuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.UnitType
import com.sunnychung.lib.multiplatform.kotlite.model.UnitValue
import com.sunnychung.lib.multiplatform.kotlite.util.wrapPrimitiveValueAsRuntimeValue
${config.imports.joinToString("") { "import $it\n" }}
${config.typeAliases.toList().joinToString { "typealias ${it.first} = ${it.second}\n" }}

abstract class Abstract${name}LibModule : LibraryModule("$name") {
    override val classes = emptyList<ProvidedClassDefinition>()
    override val properties = listOf<ExtensionProperty>(${extensionProperties.joinToString("") { "\n${it.generate(SourcePosition(name, it.position.lineNum, it.position.col), indent(8))},\n" }}
    )
    
    override val globalProperties = emptyList<GlobalProperty>()
    
    override val functions = listOf<CustomFunctionDefinition>(${functionInterfaces.joinToString("") { "\n${it.generate(SourcePosition(name, it.position.lineNum, it.position.col), indent(8))},\n" }}
    )
}
        """.trimIndent()
    }
    fun FunctionDeclarationNode.generate(position: SourcePosition, indent: String): String {
        with (ScopedDelegationCodeGenerator(
            typeParameterNodes = typeParameters,
            isNullAware = modifiers.contains(FunctionModifier.nullaware),
        )) {
            return generate(position, indent)
        }
    }
    fun PropertyDeclarationNode.generate(position: SourcePosition, indent: String): String {
        with (ScopedDelegationCodeGenerator(typeParameterNodes = typeParameters, isNullAware = false)) {
            return generate(position, indent)
        }
    }
}

internal class ScopedDelegationCodeGenerator(private val typeParameterNodes: List<TypeParameterNode>, private val isNullAware: Boolean) {
    val typeParameters = typeParameterNodes.associate {
        it.name to (it.typeUpperBound ?: TypeNode(SourcePosition.NONE, "Any", null, true))
    }
    val typeParametersUsedIn = mutableMapOf<String, String>()

    fun resolve(type: TypeNode): TypeNode {
        return type.resolveGenericParameterTypeToUpperBound(typeParameterNodes)
    }
    fun resolveForWrap(type: TypeNode): TypeNode {
        return type.resolveGenericParameterTypeToUpperBound(typeParameterNodes, isKeepTypeParameter = true)
    }
    fun FunctionDeclarationNode.generate(position: SourcePosition, indent: String): String {
        if (receiver != null && receiver!!.name in this@ScopedDelegationCodeGenerator.typeParameters) {
            typeParametersUsedIn[receiver!!.name] = "receiver"
        }
        valueParameters.forEachIndexed { index, it ->
            if (it.type.name in this@ScopedDelegationCodeGenerator.typeParameters) {
                typeParametersUsedIn[it.type.name] = "args[$index]"
            }
        }
        return """CustomFunctionDefinition(
    receiverType = ${receiver?.let { "\"${it.descriptiveName().escape()}\"" } ?: "null"},
    functionName = "${name.escape()}",
    returnType = "${returnType.descriptiveName()}",
    parameterTypes = listOf(${if (valueParameters.isNotEmpty()) "\n${valueParameters.joinToString("") { "${it.generate(indent(8))},\n" }}${indent(4)}" else ""}),
    typeParameters = ${if (typeParameters.isEmpty()) "emptyList()" else "listOf(${typeParameterNodes.joinToString("") { "\n${it.generate(indent(8))}," }}\n${indent(4)})"},
    modifiers = setOf<FunctionModifier>(${modifiers.filter { it != FunctionModifier.nullaware }.joinToString(", ") { "FunctionModifier.${it.name}" }}),
    executable = { interpreter, receiver, args, typeArgs ->
${if (receiver != null && !receiver!!.name.endsWith(".Companion")) { "        val unwrappedReceiver = ${unwrap("receiver", receiver!!)}" } else ""}
${valueParameters.mapIndexed { i, it -> "${indent(4)}val ${it.name}_ = ${unwrap("args[$i]", it.type, it.modifiers.contains(FunctionValueParameterModifier.vararg))}\n" }.joinToString("")}
        val result = ${if (receiver != null) { if (receiver!!.name.endsWith(".Companion")) { "${receiver!!.name}." } else { "unwrappedReceiver." } } else ""}$name(${
            if (valueParameters.size == 1 && valueParameters.first().modifiers.contains(FunctionValueParameterModifier.vararg)) {
                "*${valueParameters.first().name}_.${
                    if (valueParameters.first().type.name == "Byte") { "toByteArray()" } else { "toTypedArray()" }
                }"
            } else {
                valueParameters.joinToString(", ") { it.name + "_" }
            }
        })
        ${wrap("result", returnType)}
    },
    position = SourcePosition(filename = "${position.filename}", lineNum = ${position.lineNum}, col = ${position.col}),
)""".prependIndent(indent)
    }

    fun TypeParameterNode.generate(indent: String): String {
        return """TypeParameter(
    name = "$name",
    upperBound = ${typeUpperBound?.let { "${it.descriptiveName()}" } ?: "null"},
    variance = null,
)""".prependIndent(indent)
    }

    fun PropertyDeclarationNode.generate(position: SourcePosition, indent: String): String {
        return """ExtensionProperty(
    declaredName = "$name",
    receiver = "${type.descriptiveName()}",
    type = "${returnType.descriptiveName()}",
    getter = { interpreter, receiver, _ ->
        val unwrappedReceiver = ${unwrap("receiver", type)}
        val result = unwrappedReceiver.$name
        ${wrap("result", returnType)}
    },
)""".prependIndent(indent)
    }

    fun FunctionValueParameterNode.generate(indent: String): String {
        return """CustomFunctionParameter(
    declaredName = "$name",
    type = "${type.descriptiveName()}",
    isNullable = ${type.isNullable},
    isVararg = ${modifiers.contains(FunctionValueParameterModifier.vararg)},
    defaultValueExpression = ${defaultValue?.let { "\"${it.generate()}\"" } ?: "null"},
)""".prependIndent(indent)
    }

    // kotlin value -> Interpreter runtime value
    fun wrap(variableName: String, _type: TypeNode): String {
        val isTypeATypeParameter = typeParametersUsedIn.containsKey(_type.name)
        val type = resolveForWrap(_type)
        fun TypeNode.toDataTypeCode(): String {
            return if (typeParameters.containsKey(this.name)) {
                "typeArgs[\"${this.name}\"]!!.copyOf(isNullable = ${this.isNullable})"
            } else if (this.name == "Comparable") {
                val arg = this.arguments!!.first()
                arg.toDataTypeCode()
            } else if (this.isPrimitiveWithValue()) {
                "interpreter.symbolTable().${if (this.isNullable) "Nullable" else ""}${this.name}Type"
            } else if (this.isPrimitive()) {
                "${this.name}Type(isNullable = ${this.isNullable})"
            } else {
                "ObjectType(${this.name}Class.clazz, listOf<DataType>(${this.arguments?.joinToString(", ") { it.toDataTypeCode() } ?: ""}), superTypes = emptyList())"
            }
        }
        val translatedTypeName = when (type.name) {
            "Collection" -> "ListValue"
            else -> "${type.name}Value"
        }
        val wrappedValue = if (type.name == "Any" || (typeParameters.containsKey(type.name) && typeParameters[type.name]!!.name == "Any")) {
            "$variableName as RuntimeValue"
        } else if (type.name == "Nothing") {
            variableName
        } else if (isTypeATypeParameter) {
            "DelegatedValue<$variableName, (${typeParametersUsedIn[_type.name]}.type as ObjectType).clazz, listOf()>"
        } else {
            "$translatedTypeName($variableName)"
        }
        return wrappedValue
    }

    fun unwrap(variableName: String, type: TypeNode, isVararg: Boolean = false): String {
        if (isVararg) {
            val unwrapped = unwrap("it", type)
            return "$variableName.map { $unwrapped }"
        }
        if (type.name == "Any" || (typeParameters.containsKey(type.name) && typeParameters[type.name]!!.name == "Any")) {
            return variableName
        }
        return "($variableName as ${type.name}Value).value"
    }
}

private fun String.escape(): String {
    return this.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
}

private fun indent(count: Int): String = " ".repeat(count)
