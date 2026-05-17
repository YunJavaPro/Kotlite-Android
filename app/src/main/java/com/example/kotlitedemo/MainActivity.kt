package com.example.kotlitedemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sunnychung.lib.multiplatform.kotlite.KotliteInterpreter
import com.sunnychung.lib.multiplatform.kotlite.model.ExecutionEnvironment
import com.sunnychung.lib.multiplatform.kotlite.model.IntValue
import com.sunnychung.lib.multiplatform.kotlite.model.LongValue
import com.sunnychung.lib.multiplatform.kotlite.model.RuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.StringValue
import com.sunnychung.lib.multiplatform.kotlite.stdlib.AllStdLibModules

class MainActivity : AppCompatActivity() {
    
    private lateinit var codeInput: EditText
    private lateinit var outputText: TextView
    
    private val defaultCode = """
fun fib(n: Int): Long {
    if (n <= 1) return n.toLong()
    return fib(n - 1) + fib(n - 2)
}

fun factorial(n: Int): Long {
    var result = 1L
    for (i in 1..n) {
        result = result * i
    }
    return result
}

val fib10 = fib(10)
val fact5 = factorial(5)

val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val sum = doubled.sum()

val greeting = "Hello, Kotlite!"
val upperGreeting = greeting.uppercase()

// 输出结果
println("Fibonacci(10) = ${'$'}fib10")
println("Factorial(5) = ${'$'}fact5")
println("Sum = ${'$'}sum")
println("Greeting = ${'$'}upperGreeting")
    """.trimIndent()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sv = ScrollView(this)
        val mainLayout = android.widget.LinearLayout(this)
        mainLayout.orientation = android.widget.LinearLayout.VERTICAL
        mainLayout.setPadding(16, 16, 16, 16)
        
        val title = TextView(this)
        title.text = "Kotlite 解释器 Demo"
        title.textSize = 24f
        title.setPadding(0, 0, 0, 8)
        
        val desc = TextView(this)
        desc.text = "在下方输入你的 Kotlin 代码，然后点击运行按钮执行"
        desc.textSize = 14f
        desc.setPadding(0, 0, 0, 16)
        
        val codeLabel = TextView(this)
        codeLabel.text = "代码输入："
        codeLabel.textSize = 16f
        codeLabel.setPadding(0, 8, 0, 4)
        
        codeInput = EditText(this).apply {
            hint = "在此输入 Kotlin 代码..."
            setText(defaultCode)
            textSize = 13f
            typeface = android.graphics.Typeface.MONOSPACE
            minLines = 12
            maxLines = 20
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setBackgroundColor(0xFFF5F5F5.toInt())
            setPadding(16, 16, 16, 16)
        }
        
        val buttonLayout = android.widget.LinearLayout(this)
        buttonLayout.orientation = android.widget.LinearLayout.HORIZONTAL
        buttonLayout.setPadding(0, 16, 0, 16)
        
        val runButton = Button(this).apply {
            text = "运行代码"
            layoutParams = android.widget.LinearLayout.LayoutParams(
                0,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 8
            }
        }
        
        val clearButton = Button(this).apply {
            text = "清空输出"
            layoutParams = android.widget.LinearLayout.LayoutParams(
                0,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        
        val outputLabel = TextView(this)
        outputLabel.text = "执行结果："
        outputLabel.textSize = 16f
        outputLabel.setPadding(0, 8, 0, 4)
        
        outputText = TextView(this).apply {
            text = "点击「运行代码」按钮开始执行...\n"
            textSize = 13f
            typeface = android.graphics.Typeface.MONOSPACE
            setBackgroundColor(0xFF1E1E1E.toInt())
            setTextColor(0xFFE0E0E0.toInt())
            setPadding(16, 16, 16, 16)
            minLines = 10
        }
        
        runButton.setOnClickListener {
            val code = codeInput.text.toString()
            if (code.isBlank()) {
                appendOutput("请输入要执行的代码！\n")
                return@setOnClickListener
            }
            executeCode(code)
        }
        
        clearButton.setOnClickListener {
            outputText.text = "输出已清空...\n"
        }
        
        buttonLayout.addView(runButton)
        buttonLayout.addView(clearButton)
        
        mainLayout.addView(title)
        mainLayout.addView(desc)
        mainLayout.addView(codeLabel)
        mainLayout.addView(codeInput)
        mainLayout.addView(buttonLayout)
        mainLayout.addView(outputLabel)
        mainLayout.addView(outputText)
        sv.addView(mainLayout)
        
        setContentView(sv)
    }
    
    private fun executeCode(code: String) {
        appendOutput("\n\n")
        appendOutput("开始执行代码...\n\n")
        
        val startTime = System.currentTimeMillis()
        val output = StringBuilder()
        
        try {
            val env = ExecutionEnvironment().apply {
                install(AllStdLibModules { msg -> 
                    runOnUiThread {
                        appendOutput(msg)
                    }
                    output.append(msg)
                })
            }
            
            val interpreter = KotliteInterpreter(
                filename = "UserScript",
                code = code,
                executionEnvironment = env
            )
            
            interpreter.eval()
            
            val symbolTable = interpreter.symbolTable()
            val variables = collectVariables(symbolTable)
            
            val endTime = System.currentTimeMillis()
            
            appendOutput("\n\n")
            appendOutput("执行成功！\n")
            appendOutput("耗时: ${endTime - startTime}ms\n")
            
            if (variables.isNotEmpty()) {
                appendOutput("\n变量值:\n")
                variables.forEach { (name, value) ->
                    appendOutput("  $name = $value\n")
                }
            }
            
        } catch (e: Exception) {
            appendOutput("\n执行错误!\n")
            appendOutput("错误类型: ${e.javaClass.simpleName}\n")
            appendOutput("错误信息: ${e.message}\n")
            
            e.stackTrace?.take(5)?.forEach { element ->
                appendOutput("  at ${element.fileName}:${element.lineNumber}\n")
            }
        }
        
        appendOutput("\n\n")
    }
    
    private fun collectVariables(symbolTable: com.sunnychung.lib.multiplatform.kotlite.model.SymbolTable): List<Pair<String, String>> {
        val variables = mutableListOf<Pair<String, String>>()
        
        fun collectProperty(name: String, value: RuntimeValue) {
            val displayValue = when (value) {
                is IntValue -> value.value.toString()
                is LongValue -> value.value.toString()
                is StringValue -> "\"${value.value}\""
                else -> value.toString()
            }
            variables.add(name to displayValue)
        }
        
        try {
            symbolTable.findPropertyByDeclaredName("fib10")?.let {
                val holder = it
                collectProperty("fib10", holder as RuntimeValue)
            }
        } catch (e: Exception) { }
        
        try {
            val allNames = listOf("fib10", "fact5", "sum", "greeting", "upperGreeting", 
                                  "fib", "factorial", "numbers", "doubled")
            allNames.forEach { name ->
                try {
                    val holder = symbolTable.findPropertyByDeclaredName(name)
                    if (holder is RuntimeValue && holder !is com.sunnychung.lib.multiplatform.kotlite.model.UnitValue) {
                        collectProperty(name, holder)
                    }
                } catch (e: Exception) {
                    // 忽略无法获取的属性
                }
            }
        } catch (e: Exception) {
            // 忽略收集错误
        }
        
        return variables
    }
    
    private fun appendOutput(text: String) {
        runOnUiThread {
            outputText.append(text)
        }
    }
}
