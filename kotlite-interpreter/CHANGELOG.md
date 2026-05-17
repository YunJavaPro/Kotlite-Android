# Kotlite 解释器变更日志

本项目的所有重大变更都将记录在此文件中。

该格式基于[Keep a Changelog](https://keepachangelog.com/en/1.1.0/)，
并且本项目遵循[语义化版本控制](https://semver.org/spec/v2.0.0.html)。

## [未发布]

目前还没有消息。

## [1.1.2] - 2024-07-09

＃＃＃ 固定的

- 解析包含字符串字面量 `"|"` 的代码时，不应抛出 UnsupportedOperationException 异常：“不支持运算符 `|`”。

## [1.1.1] - 2024-07-09

＃＃＃ 固定的

- 解析包含字符串字面量 `"|"` 的代码时，不应抛出 UnsupportedOperationException 异常：“不支持运算符 `|`”。

## [1.1.0] - 2024-04-27

### 已添加

- 将 `AnyType` 属性更改为 `SymbolTable`，以保持 API 一致性
- `String.toDataType` 便捷的扩展函数
- 类 `SourcePosition` 的属性 `index`
- 类 `Token` 的属性 `endExclusive`
- 函数 `Lexer.currentMode`
- 可选构造函数参数 `isParseComment`，用于传递给 `Lexer` 函数。如果为 `true`，则会为注释创建 `Token` 实例。默认值为 `false`。
- `MultipleLibModules` -- 从 kotlite-stdlib 库中移出

### 已更改

- 向枚举类 `TokenType` 中添加新的枚举条目 `Comment`
- `TypeMismatchException` 的错误消息现在包含类型参数
- 现在在评估 `as` 运算符、赋值类型检查和返回值类型检查时，类型参数将被清除。

＃＃＃ 固定的

- 如果 lambda 表达式被用作函数值参数，而该函数值参数又被用作另一个函数值参数，则无法访问 lambda 表达式的参数，例如 `func(list.first { it } } } } } } } >= 7 })`
- 扩展属性的可空解析不正确
- `IntRange.joinToString` 产生错误的字符串值
- `MutableMapValue` 的函数签名
日志记录器不再是共享实例，而是专用实例。

## [1.0.0] - 2024-04-08

### 已添加

- 首次发布
