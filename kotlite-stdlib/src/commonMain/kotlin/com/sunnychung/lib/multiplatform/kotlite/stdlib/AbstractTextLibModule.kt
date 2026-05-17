package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.BooleanValue
import com.sunnychung.lib.multiplatform.kotlite.model.CharValue
import com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.DoubleValue
import com.sunnychung.lib.multiplatform.kotlite.model.ExtensionProperty
import com.sunnychung.lib.multiplatform.kotlite.model.GlobalProperty
import com.sunnychung.lib.multiplatform.kotlite.model.IntValue
import com.sunnychung.lib.multiplatform.kotlite.model.ListValue
import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule
import com.sunnychung.lib.multiplatform.kotlite.model.NullValue
import com.sunnychung.lib.multiplatform.kotlite.model.ProvidedClassDefinition
import com.sunnychung.lib.multiplatform.kotlite.model.RuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.SourcePosition
import com.sunnychung.lib.multiplatform.kotlite.model.StringValue

abstract class AbstractTextLibModule : LibraryModule("Text") {
    override val classes: List<ProvidedClassDefinition> = emptyList()
    override val properties: List<ExtensionProperty> = listOf(
        ExtensionProperty(
            declaredName = "length",
            receiver = "String",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                IntValue((receiver as StringValue).value.length, interpreter.symbolTable())
            },
        ),
        ExtensionProperty(
            declaredName = "lastIndex",
            receiver = "String",
            type = "Int",
            getter = { interpreter, receiver, _ ->
                val s = (receiver as StringValue).value
                IntValue(s.length - 1, interpreter.symbolTable())
            },
        ),
    )
    override val globalProperties: List<GlobalProperty> = emptyList()

    private fun RuntimeValue.requireString(): String = (this as StringValue).value
    private fun RuntimeValue.requireInt(): Int = (this as IntValue).value
    private fun RuntimeValue.requireChar(): Char = (this as CharValue).value
    private fun RuntimeValue.requireBoolean(): Boolean = (this as BooleanValue).value

    override val functions: List<CustomFunctionDefinition> = listOf(
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "substring",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("startIndex", "Int"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("endIndex", "Int", defaultValueExpression = "length"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val start = args[0].requireInt()
                val end = args[1].requireInt()
                StringValue(s.substring(start, end), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "uppercase",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                StringValue((receiver as StringValue).value.uppercase(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "lowercase",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                StringValue((receiver as StringValue).value.lowercase(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "padStart",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("length", "Int"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("padChar", "Char", defaultValueExpression = "' '"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val len = args[0].requireInt()
                val ch = args[1].requireChar()
                StringValue(s.padStart(len, ch), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "padEnd",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("length", "Int"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("padChar", "Char", defaultValueExpression = "' '"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val len = args[0].requireInt()
                val ch = args[1].requireChar()
                StringValue(s.padEnd(len, ch), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "replace",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("oldValue", "Char"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("newValue", "Char"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val oldValue = args[0].requireChar()
                val newValue = args[1].requireChar()
                val ignoreCase = args[2].requireBoolean()
                StringValue(s.replace(oldValue, newValue, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "replace",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("oldValue", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("newValue", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val oldValue = args[0].requireString()
                val newValue = args[1].requireString()
                val ignoreCase = args[2].requireBoolean()
                StringValue(s.replace(oldValue, newValue, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "replaceFirst",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("oldValue", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("newValue", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val oldValue = args[0].requireString()
                val newValue = args[1].requireString()
                val ignoreCase = args[2].requireBoolean()
                val result = if (ignoreCase) {
                    val regex = Regex(Regex.escape(oldValue), RegexOption.IGNORE_CASE)
                    regex.replaceFirst(s, newValue)
                } else {
                    s.replaceFirst(oldValue, newValue)
                }
                StringValue(result, interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "trim",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                StringValue((receiver as StringValue).value.trim(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "trimStart",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                StringValue((receiver as StringValue).value.trimStart(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "trimEnd",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                StringValue((receiver as StringValue).value.trimEnd(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "split",
            returnType = "List<String>",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("delimiter", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("limit", "Int", defaultValueExpression = "0"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val delimiter = args[0].requireString()
                val ignoreCase = args[1].requireBoolean()
                val limit = args[2].requireInt()
                val parts = s.split(delimiter, ignoreCase = ignoreCase, limit = limit)
                val symbolTable = interpreter.symbolTable()
                val list = parts.map { StringValue(it, symbolTable) }
                ListValue(list, symbolTable.StringType, symbolTable)
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "substringAfter",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("delimiter", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("missingDelimiterValue", "String", defaultValueExpression = "this"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val delimiter = args[0].requireString()
                val missingDelimiterValue = args[1].requireString()
                StringValue(s.substringAfter(delimiter, missingDelimiterValue), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "substringAfterLast",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("delimiter", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("missingDelimiterValue", "String", defaultValueExpression = "this"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val delimiter = args[0].requireString()
                val missingDelimiterValue = args[1].requireString()
                StringValue(s.substringAfterLast(delimiter, missingDelimiterValue), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "substringBefore",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("delimiter", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("missingDelimiterValue", "String", defaultValueExpression = "this"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val delimiter = args[0].requireString()
                val missingDelimiterValue = args[1].requireString()
                StringValue(s.substringBefore(delimiter, missingDelimiterValue), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "substringBeforeLast",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("delimiter", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("missingDelimiterValue", "String", defaultValueExpression = "this"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val delimiter = args[0].requireString()
                val missingDelimiterValue = args[1].requireString()
                StringValue(s.substringBeforeLast(delimiter, missingDelimiterValue), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "contains",
            returnType = "Boolean",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("other", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val other = args[0].requireString()
                val ignoreCase = args[1].requireBoolean()
                BooleanValue(s.contains(other, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "contains",
            returnType = "Boolean",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("other", "Char"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val other = args[0].requireChar()
                val ignoreCase = args[1].requireBoolean()
                BooleanValue(s.contains(other, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "startsWith",
            returnType = "Boolean",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("prefix", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val prefix = args[0].requireString()
                val ignoreCase = args[1].requireBoolean()
                BooleanValue(s.startsWith(prefix, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "endsWith",
            returnType = "Boolean",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("suffix", "String"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val suffix = args[0].requireString()
                BooleanValue(s.endsWith(suffix), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "indexOf",
            returnType = "Int",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("string", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("startIndex", "Int", defaultValueExpression = "0"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val string = args[0].requireString()
                val startIndex = args[1].requireInt()
                val ignoreCase = args[2].requireBoolean()
                IntValue(s.indexOf(string, startIndex = startIndex, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "lastIndexOf",
            returnType = "Int",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("string", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("startIndex", "Int", defaultValueExpression = "length"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("ignoreCase", "Boolean", defaultValueExpression = "false"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                val string = args[0].requireString()
                val startIndex = args[1].requireInt()
                val ignoreCase = args[2].requireBoolean()
                IntValue(s.lastIndexOf(string, startIndex = startIndex, ignoreCase = ignoreCase), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "drop",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("n", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.drop(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "dropLast",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("n", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.dropLast(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "take",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("n", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.take(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "takeLast",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("n", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.takeLast(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "repeat",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("n", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.repeat(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "reversed",
            returnType = "String",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.reversed(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "removePrefix",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("prefix", "String"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.removePrefix(args[0].requireString()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "removeSuffix",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("suffix", "String"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.removeSuffix(args[0].requireString()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "removeSurrounding",
            returnType = "String",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("prefix", "String"),
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("suffix", "String"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val s = (receiver as StringValue).value
                StringValue(s.removeSurrounding(args[0].requireString(), args[1].requireString()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "isEmpty",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as StringValue).value.isEmpty(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "isNotEmpty",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as StringValue).value.isNotEmpty(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "isBlank",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as StringValue).value.isBlank(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "isNotBlank",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as StringValue).value.isNotBlank(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "isDigit",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as CharValue).value.isDigit(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "isLetter",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as CharValue).value.isLetter(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "isLetterOrDigit",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as CharValue).value.isLetterOrDigit(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "isWhitespace",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                BooleanValue((receiver as CharValue).value.isWhitespace(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "uppercaseChar",
            returnType = "Char",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                CharValue((receiver as CharValue).value.uppercaseChar(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "Char",
            functionName = "lowercaseChar",
            returnType = "Char",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                CharValue((receiver as CharValue).value.lowercaseChar(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String?",
            functionName = "toBoolean",
            returnType = "Boolean",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val value = when (receiver) {
                    NullValue -> false
                    else -> (receiver as StringValue).value.toBoolean()
                }
                BooleanValue(value, interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toInt",
            returnType = "Int",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                IntValue((receiver as StringValue).value.toInt(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toInt",
            returnType = "Int",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("radix", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                IntValue((receiver as StringValue).value.toInt(args[0].requireInt()), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toIntOrNull",
            returnType = "Int?",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val value = (receiver as StringValue).value.toIntOrNull()
                if (value == null) NullValue else IntValue(value, interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toIntOrNull",
            returnType = "Int?",
            parameterTypes = listOf(
                com.sunnychung.lib.multiplatform.kotlite.model.CustomFunctionParameter("radix", "Int"),
            ),
            executable = { interpreter, receiver, args, _ ->
                val value = (receiver as StringValue).value.toIntOrNull(args[0].requireInt())
                if (value == null) NullValue else IntValue(value, interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toDouble",
            returnType = "Double",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                DoubleValue((receiver as StringValue).value.toDouble(), interpreter.symbolTable())
            },
        ),
        CustomFunctionDefinition(
            position = SourcePosition(name, 1, 1),
            receiverType = "String",
            functionName = "toDoubleOrNull",
            returnType = "Double?",
            parameterTypes = emptyList(),
            executable = { interpreter, receiver, _, _ ->
                val value = (receiver as StringValue).value.toDoubleOrNull()
                if (value == null) NullValue else DoubleValue(value, interpreter.symbolTable())
            },
        ),
    )
}