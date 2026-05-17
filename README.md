# Kotlite-Android

一个基于 [Kotlite](https://github.com/sunny-chung/kotlite) 的 Android 演示应用，可以在 Android 设备上运行 Kotlin 子集代码的解释器。

## 什么是 Kotlite？

**Kotlite** 是一个开源的类型安全编程语言，拥有 [Kotlin](https://kotlinlang.org/) 脚本变体的丰富子集。它附带标准库，这些库是 Kotlin Multiplatform/Common 标准库和一些第三方库的子集。

**Kotlite 解释器** 是一个轻量级的 Kotlin Multiplatform 库，用于解释和执行用 Kotlite 编写的代码，并在主机运行时环境和嵌入式运行时环境之间建立桥梁。

### 主要特性

- 支持 Kotlin 1.9 语言和标准库的一个子集
- 支持编写复杂的泛型代码
- 可嵌入 — 它是一个库
- 安全 — 可白名单/黑名单类、函数、属性；标准库中没有文件或网络 I/O 或操作系统 API
- 多平台运行（JVM、JS、Android、iOS、macOS、watchOS、tvOS）
- 可扩展并允许与主机交互
- 轻量级 — Kotlite 加上所有平台标准库的总和小于 10 MB
- 执行前进行语义分析（变量访问和类型验证）
- 类型推断
- 经过良好测试 — 每个平台有超过一千个手工编写的单元测试

### 支持的语言特性

- 函数定义与递归调用
- 变量声明与类型推断
- 类、接口、枚举、伴生对象
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
- 空值安全（`?`、`?.`、`?:`）
- 运算符重载

### 不支持的特性

- 协程（suspend 函数）
- 自定义类声明（主要用于内置类型和标准库）
- 完整的 OOP 设计

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
git clone https://github.com/YunJavaPro/Kotlite-Android.git
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

## 相关链接

- [Kotlite GitHub 仓库](https://github.com/sunny-chung/kotlite) - Kotlite 原始项目
- [Kotlite 中文文档](https://yunjavapro.github.io/kotlite-zh/) - Kotlite 中文文档网站
- [Kotlite 中文文档仓库](https://github.com/YunJavaPro/kotlite-zh) - 中文文档源码
- [Kotlite 在线脚本测试](https://yunjavapro.github.io/kotlite-zh/demo/index.html) - 在浏览器中测试 Kotlite 代码

## 依赖

- [Kotlite Interpreter](https://github.com/sunny-chung/kotlite) - Kotlin 子集解释器核心
- [Kotlite Stdlib Processor Plugin](https://github.com/sunny-chung/kotlite) - 标准库代码生成插件
- [Kermit](https://github.com/touchlab/Kermit) - 多平台日志库
- [kdatetime-multiplatform](https://github.com/sunny-chung/kdatetime-multiplatform) - Kotlin 日期时间库
- [uuid](https://github.com/benasher44/uuid) - Kotlin UUID 生成库

## 许可证

请参阅原始 [Kotlite](https://github.com/sunny-chung/kotlite) 项目的许可证。
