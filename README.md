# Kotlite-Android

一个基于 [Kotlite](https://github.com/sunny-chung/kotlite) 的 Android 演示应用，可以在 Android 设备上运行 Kotlin 子集代码的解释器。

## 项目简介

Kotlite 是一个轻量级的 Kotlin 子集解释器，支持 Kotlin 语法的一个子集，包括：

- 函数定义与递归调用
- 变量声明与类型推断
- 类、接口、枚举
- Lambda 表达式与高阶函数
- 集合操作（List、Map、Set）
- 范围表达式（Range）
- 字符串模板与操作
- 正则表达式
- 日期时间处理
- UUID 生成
- 异常处理（try-catch-finally）
- `when` 表达式
- 扩展函数与中缀函数
- 泛型支持

## 项目结构

```
Kotlite-Android/
├── app/                      # Android 演示应用
│   └── src/main/java/.../MainActivity.kt
├── kotlite-interpreter/      # Kotlite 解释器核心库
│   └── src/commonMain/       # 词法分析、语法分析、语义分析、解释执行
├── kotlite-stdlib/           # Kotlite 标准库
│   └── src/commonMain/       # 核心库、集合、数学、文本、日期时间等模块
├── build.gradle.kts          # 根级构建配置
├── settings.gradle.kts       # Gradle 模块设置
└── gradle.properties         # Gradle 属性配置
```

### 模块说明

| 模块 | 说明 |
|------|------|
| `app` | Android 演示应用，提供代码输入界面，可实时运行 Kotlite 代码并查看输出结果 |
| `kotlite-interpreter` | Kotlite 解释器核心，包含词法分析器（Lexer）、语法分析器（Parser）、语义分析器（SemanticAnalyzer）和解释器（Interpreter） |
| `kotlite-stdlib` | Kotlite 标准库，提供核心函数、集合、数学运算、文本处理、日期时间、正则表达式、UUID 等模块 |

## 技术栈

- **语言**：Kotlin 2.0.21
- **最低 SDK**：API 26 (Android 8.0)
- **目标 SDK**：API 36
- **构建工具**：Gradle (Kotlin DSL)
- **Android Gradle Plugin**：8.3.2
- **Java 版本**：17

## 快速开始

### 环境要求

- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK（compileSdk 36）

### 构建与运行

1. 克隆仓库：

```bash
git clone https://github.com/<your-username>/Kotlite-Android.git
cd Kotlite-Android
```

2. 用 Android Studio 打开项目

3. 连接 Android 设备或启动模拟器

4. 点击运行按钮

### 使用方法

应用启动后，界面提供一个代码编辑区域，默认包含示例代码。你可以：

1. 在编辑区输入或修改 Kotlin 代码
2. 点击「运行代码」按钮执行
3. 在下方查看执行输出和变量值
4. 点击「清空输出」按钮清除结果

默认示例代码演示了递归函数（Fibonacci、阶乘）、集合操作和字符串处理等功能。

## 代码示例

```kotlin
// 递归计算 Fibonacci 数列
fun fib(n: Int): Long {
    if (n <= 1) return n.toLong()
    return fib(n - 1) + fib(n - 2)
}

// 阶乘计算
fun factorial(n: Int): Long {
    var result = 1L
    for (i in 1..n) {
        result = result * i
    }
    return result
}

val fib10 = fib(10)
val fact5 = factorial(5)

// 集合操作
val numbers = listOf(1, 2, 3, 4, 5)
val doubled = numbers.map { it * 2 }
val sum = doubled.sum()

// 字符串操作
val greeting = "Hello, Kotlite!"
val upperGreeting = greeting.uppercase()

println("Fibonacci(10) = $fib10")
println("Factorial(5) = $fact5")
println("Sum = $sum")
println("Greeting = $upperGreeting")
```

## 依赖

- [Kotlite Interpreter](https://github.com/sunny-chung/kotlite) - Kotlin 子集解释器核心
- [Kotlite Stdlib Processor Plugin](https://github.com/sunny-chung/kotlite) - 标准库代码生成插件
- [Kermit](https://github.com/touchlab/Kermit) - 多平台日志库
- [kdatetime-multiplatform](https://github.com/sunny-chung/kdatetime-multiplatform) - Kotlin 日期时间库
- [uuid](https://github.com/benasher44/uuid) - Kotlin UUID 生成库

## 许可证

请参阅原始 [Kotlite](https://github.com/sunny-chung/kotlite) 项目的许可证。
