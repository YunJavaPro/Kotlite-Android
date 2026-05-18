package com.example.kotlitedemo

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.sunnychung.lib.multiplatform.kotlite.KotliteInterpreter
import com.sunnychung.lib.multiplatform.kotlite.model.ExecutionEnvironment
import com.sunnychung.lib.multiplatform.kotlite.model.IntValue
import com.sunnychung.lib.multiplatform.kotlite.model.LongValue
import com.sunnychung.lib.multiplatform.kotlite.model.RuntimeValue
import com.sunnychung.lib.multiplatform.kotlite.model.StringValue
import com.sunnychung.lib.multiplatform.kotlite.stdlib.AllStdLibModules
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.max
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var codeInput: EditText
    private lateinit var outputText: TextView
    private lateinit var lineNumbersText: TextView
    private lateinit var sampleCodeSpinner: AutoCompleteTextView
    private var selectedSampleIndex = 0
    
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val inputStream = contentResolver.openInputStream(it)
                if (inputStream != null) {
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val code = reader.readText()
                    codeInput.setText(code)
                    reader.close()
                    inputStream.close()
                }
            } catch (e: Exception) {
                appendOutput("导入文件失败: ${e.message}\n")
            }
        }
    }

    private val rawSamples = listOf(
        "基础示例 (Fibonacci)" to """
fun fib(n: Int): Long {
    if (n <= 1) return n.toLong()
    return fib(n - 1) + fib(n - 2)
}

val fib10 = fib(10)
println("Fibonacci(10) = ${'$'}fib10")
        """.trimIndent(),
        
        "集合与循环操作" to """
val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val sum = doubled.sum()

println("原始数据: ${'$'}numbers")
println("翻倍数据: ${'$'}doubled")
println("总和结果: ${'$'}sum")

val map = mapOf("苹果" to 5, "香蕉" to 10)
for (entry in map) {
    println("${'$'}{entry.key} 数量: ${'$'}{entry.value}")
}
        """.trimIndent(),

        "类与继承" to """
open class Animal(val name: String) {
    open fun speak(): Unit { println("...") }
}

class Dog(name: String) : Animal(name) {
    override fun speak(): Unit { println("${'$'}name 汪汪叫!") }
}

class Cat(name: String) : Animal(name) {
    override fun speak(): Unit { println("${'$'}name 喵喵叫!") }
}

val pets = listOf(Dog("小黑"), Cat("小白"))
pets.forEach { it.speak() }
        """.trimIndent(),

        "异常处理 (Try-Catch)" to """
fun divide(a: Int, b: Int): Int {
    if (b == 0) throw Exception("不能除以 0 呀！")
    return a / b
}

try {
    val result = divide(10, 0)
    println("结果: ${'$'}result")
} catch (e: Exception) {
    println("捕获到异常: ${'$'}{e.message}")
} finally {
    println("运行结束。")
}
        """.trimIndent(),

        "扩展函数与泛型" to """
// 给 Int 添加扩展函数
fun Int.isEven(): Boolean = this % 2 == 0
fun Int.triple(): Int = this * 3

// 泛型函数
fun <T> printRepeated(item: T, count: Int) {
    repeat(count) { println(item) }
}

println("4 是偶数吗？ ${'$'}{4.isEven()}")
println("5 的三倍是: ${'$'}{5.triple()}")
printRepeated("Kotlite!", 3)
        """.trimIndent(),

        "日期与时间 (KDateTime)" to """
val now = KZonedInstant.nowAtLocalZoneOffset()
println("当前时间: ${'$'}{now.format(KDateTimeFormat.ISO8601_DATETIME.pattern)}")

val duration = 2.hours() + 30.minutes()
println("增加时长: ${'$'}{duration.toMinutes()} 分钟")
        """.trimIndent(),

        "文本与正则 (Regex)" to """
val text = "Kotlite 诞生于 2024 年！"
val regex = Regex("[0-9]+")

val result = text.replace(regex, "xxxx")
println("原始文本: ${'$'}text")
println("替换文本: ${'$'}result")

val isMatch = "12345".matches(Regex("^[0-9]+${'$'}"))
println("纯数字匹配结果: ${'$'}isMatch")
        """.trimIndent(),

        "内置数学库 (Math)" to """
val pi = PI
val radius = 5.0
val area = pi * radius.pow(2)

println("圆的面积: ${'$'}area")
println("sin(30°) = ${'$'}{sin(pi / 6)}")
        """.trimIndent(),

        "UUID 生成" to """
val id1 = uuidString()
val id2 = uuid4().toString()

println("生成的 UUID 1: ${'$'}id1")
println("生成的 UUID 2: ${'$'}id2")
        """.trimIndent(),

        "闭包与状态封装 (Closure)" to """
// 闭包捕获局部变量，实现内部状态保存
fun createCounter(initial: Int, step: Int): () -> Int {
    var count = initial
    return {
        val current = count
        count += step
        current
    }
}

val counterA = createCounter(0, 1)
val counterB = createCounter(100, 10)

println("计数器A连续调用: ${'$'}{counterA()}, ${'$'}{counterA()}, ${'$'}{counterA()}")
println("计数器B连续调用: ${'$'}{counterB()}, ${'$'}{counterB()}, ${'$'}{counterB()}")
        """.trimIndent(),

        "运算符重载与中缀函数" to """
class Vector(val x: Int, val y: Int) {
    // 重载 + 和 * 运算符
    operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)
    operator fun times(scalar: Int): Vector = Vector(x * scalar, y * scalar)
    
    // 定义中缀函数计算点积
    infix fun dot(other: Vector): Int = x * other.x + y * other.y
    
    override fun toString(): String = "(${'$'}x, ${'$'}y)"
}

val v1 = Vector(2, 3)
val v2 = Vector(4, 5)

println("v1 + v2 = ${'$'}{v1 + v2}")
println("v1 * 3 = ${'$'}{v1 * 3}")
println("v1 dot v2 = ${'$'}{v1 dot v2}")
        """.trimIndent(),

        "记忆化递归 (Memoization)" to """
val cache = mutableMapOf<Int, Long>()

// 使用动态规划(缓存)解决深层递归性能问题
fun memoizedFib(n: Int): Long {
    if (n <= 1) return n.toLong()
    if (n in cache) return cache[n]!!
    
    val result = memoizedFib(n - 1) + memoizedFib(n - 2)
    cache[n] = result
    return result
}

val start = KZonedInstant.nowAtLocalZoneOffset().toEpochMilliseconds()
val result = memoizedFib(80) 
val end = KZonedInstant.nowAtLocalZoneOffset().toEpochMilliseconds()

println("Fib(80) = ${'$'}result")
println("计算耗时: ${'$'}{end - start} ms (使用缓存极速完成)")
        """.trimIndent(),

        "复杂集合转换 (Map-Reduce)" to """
class Order(val user: String, val amount: Double)

val orders = listOf(
    Order("Alice", 120.5), Order("Bob", 45.0),
    Order("Alice", 75.2), Order("Charlie", 200.0),
    Order("Bob", 65.5)
)

// 链式处理：分组 -> 映射提取总额 -> 排序
val userTotals = orders.groupBy { it.user }
    .mapValues { entry -> entry.value.map { it.amount }.sum() }
    .toList()
    .sortedByDescending { it.second }

println("=== 用户消费排行 ===")
userTotals.forEach { 
    println("${'$'}{it.first}: ¥${'$'}{it.second}") 
}
        """.trimIndent(),

        "DSL 构建器模式 (HTML Builder)" to """
class HtmlBuilder {
    var content = ""
    fun h1(text: String) { content += "<h1>${'$'}text</h1>\n" }
    fun p(block: () -> String) { content += "<p>${'$'}{block()}</p>\n" }
    override fun toString(): String = content
}

// 带接收者的函数类型扩展
fun html(block: HtmlBuilder.() -> Unit): String {
    val builder = HtmlBuilder()
    builder.block()
    return builder.toString()
}

val page = html {
    h1("Welcome to Kotlite DSL")
    p { "This is built using a DSL-like syntax." }
    p { "Very elegant!" }
}

println(page)
        """.trimIndent(),

        "二叉树与深度优先遍历" to """
open class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}

// 递归遍历算法
fun traverse(node: Node?, result: MutableList<Int>) {
    if (node == null) return
    val n = node!!
    traverse(n.left, result)
    result.add(n.value)
    traverse(n.right, result)
}

val root = Node(5)
root.left = Node(3).apply { left = Node(1); right = Node(4) }
root.right = Node(8).apply { left = Node(6); right = Node(9) }

val list = mutableListOf<Int>()
traverse(root, list)
println("二叉树中序遍历结果: ${'$'}list")
        """.trimIndent(),

        "埃拉托斯特尼素数筛法" to """
// 高效找出 1 到 max 的所有素数
fun findPrimes(max: Int): List<Int> {
    val isPrime = MutableList(max + 1) { true }
    isPrime[0] = false
    isPrime[1] = false
    
    var p = 2
    while (p * p <= max) {
        if (isPrime[p]) {
            var i = p * p
            while (i <= max) {
                isPrime[i] = false
                i += p
            }
        }
        p++
    }
    return (2..max).filter { isPrime[it] }
}

val primes = findPrimes(100)
println("100以内的素数共 ${'$'}{primes.size} 个:")
println(primes.joinToString(", "))
        """.trimIndent(),

        "多态与分支类型判定 (When)" to """
open class Shape(val name: String)
class Circle(val radius: Double) : Shape("圆形")
class Rectangle(val width: Double, val height: Double) : Shape("矩形")
class Triangle(val base: Double, val height: Double) : Shape("三角形")

// when 表达式与显式类型强转
fun calculateArea(shape: Shape): Double = when (shape) {
    is Circle -> PI * (shape as Circle).radius.pow(2)
    is Rectangle -> {
        val r = shape as Rectangle
        r.width * r.height
    }
    is Triangle -> {
        val t = shape as Triangle
        t.base * t.height / 2.0
    }
    else -> 0.0
}

val shapes = listOf(Circle(5.0), Rectangle(4.0, 6.0), Triangle(3.0, 8.0))

shapes.forEach { 
    val area = calculateArea(it)
    println("${'$'}{it.name} 面积: ${'$'}area")
}
        """.trimIndent(),

        "泛型 Result 与 Monad 封装" to """
open class Result<T>
class Success<T>(val data: T) : Result<T>()
class Failure<T>(val error: String) : Result<T>()

// 泛型函数的扩展：实现类似 Monad 的 map 操作
fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Success<*> -> Success<R>(transform((this as Success<T>).data))
        is Failure<*> -> Failure<R>((this as Failure<T>).error)
        else -> Failure<R>("Unknown Error")
    }
}

val res1: Result<Int> = Success<Int>(10)
val res2: Result<Int> = Failure<Int>("网络超时")

val out1 = res1.map { it * 2 }.map { "计算成功: ${'$'}it" }
val out2 = res2.map { it * 2 }.map { "计算成功: ${'$'}it" }

fun printRes(r: Result<String>) {
    when (r) {
        is Success<*> -> println((r as Success<String>).data)
        is Failure<*> -> println("失败警告: ${'$'}{(r as Failure<String>).error}")
        else -> println("未知状态")
    }
}

printRes(out1)
printRes(out2)
        """.trimIndent(),

        "作用域函数高级链式调用" to """
class Config {
    var host = ""
    var port = 0
    var useSsl = false
    override fun toString(): String = "[${'$'}host:${'$'}port, SSL:${'$'}useSsl]"
}

// 连续组合使用 apply, also, run 和 with
val finalConfig = Config().apply {
    host = "api.kotlite.org"
    port = 8080
}.also {
    println("配置初始化中: ${'$'}it")
}.run {
    useSsl = true
    port = 443
    this
}

val url = with(finalConfig) {
    val scheme = if (useSsl) "https" else "http"
    "${'$'}scheme://${'$'}host:${'$'}port/v1/data"
}

println("最终生成的请求地址: ${'$'}url")
        """.trimIndent()
    )

    private val samples: Map<String, String> = rawSamples.mapIndexed { index, pair ->
        "${index + 1}. ${pair.first}" to pair.second
    }.toMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        codeInput = findViewById(R.id.codeInput)
        outputText = findViewById(R.id.outputText)
        lineNumbersText = findViewById(R.id.lineNumbersText)
        sampleCodeSpinner = findViewById(R.id.sampleCodeSpinner)

        findViewById<Button>(R.id.runButton).setOnClickListener {
            outputText.text = ""
            val code = codeInput.text.toString()
            if (code.isBlank()) {
                appendOutput("请输入要执行的代码！\n")
                return@setOnClickListener
            }
            executeCode(code)
        }

        findViewById<Button>(R.id.clearCodeButton).setOnClickListener {
            codeInput.setText("")
        }

        findViewById<Button>(R.id.clearOutputButton).setOnClickListener {
            outputText.text = ""
        }

        findViewById<Button>(R.id.importFileButton).setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        setupLineNumbers()
        setupSampleCodes()
    }

    private fun setupLineNumbers() {
        val updateLineNumbers = {
            val lineCount = max(1, codeInput.text.toString().split("\n").size)
            val lineNumbers = java.lang.StringBuilder()
            for (i in 1..lineCount) {
                lineNumbers.append(i).append("\n")
            }
            lineNumbersText.text = lineNumbers.toString()
        }

        codeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateLineNumbers()
            }
        })

        // Initial setup
        codeInput.post { updateLineNumbers() }
    }

    private fun setupSampleCodes() {
        val keys = samples.keys.toTypedArray()

        sampleCodeSpinner.setText(keys[selectedSampleIndex], false)
        codeInput.setText(samples[keys[selectedSampleIndex]])

        sampleCodeSpinner.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("请选择预设代码")
                .setSingleChoiceItems(keys, selectedSampleIndex) { dialog, which ->
                    selectedSampleIndex = which
                    sampleCodeSpinner.setText(keys[which], false)
                    codeInput.setText(samples[keys[which]])
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun executeCode(code: String) {
        val startTime = System.currentTimeMillis()
        val output = java.lang.StringBuilder()

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
            appendOutput("耗时: ${endTime - startTime}ms\n")

            if (variables.isNotEmpty()) {
                appendOutput("\n[作用域变量监控]:\n")
                variables.forEach { (name, value) ->
                    appendOutput("  $name = $value\n")
                }
            }

        } catch (e: Exception) {
            appendOutput("\n执行异常!\n")
            appendOutput("错误类型: ${e.javaClass.simpleName}\n")
            appendOutput("错误信息: ${e.message}\n")

            e.stackTrace?.take(5)?.forEach { element ->
                appendOutput("  at ${element.fileName}:${element.lineNumber}\n")
            }
        }
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
            val allNames = listOf("fib10", "fact5", "sum", "greeting", "upperGreeting", 
                                  "numbers", "doubled", "pi", "sin30", "now", "id", "area")
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