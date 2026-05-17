# Kotlite 标准库更新日志

本项目的所有重大变更都将记录在此文件中。

该格式基于[Keep a Changelog](https://keepachangelog.com/en/1.1.0/)，
并且本项目遵循[语义化版本控制](https://semver.org/spec/v2.0.0.html)。

## [未发布]

目前还没有消息。

## [1.1.0] - 2024-04-27

### 已添加

- UUID 库模块
- 全局函数 `setKotliteStdlibLogMinLevel(severity: Severity)`

### 已移除

- `MultipleLibModules` -- 它已被移至 kotlite-interpreter 库中

＃＃＃ 固定的

日志记录器不再是共享实例，而是专用实例。

## [1.0.0] - 2024-04-08

### 已添加

- 首次发布
