package com.sunnychung.lib.multiplatform.kotlite.stdlib

import com.sunnychung.lib.multiplatform.kotlite.model.MultipleLibModules
import com.sunnychung.lib.multiplatform.kotlite.model.LibraryModule

class AllStdLibModules(outputToConsoleFunction: (String) -> Unit = { print(it) }) : MultipleLibModules(
    name = "AllStdlib",
    modules = buildList {
        addAll(StdLibModuleRegistry.getAllNonIOModules())
        add(object : IOLibModule() {
            override fun outputToConsole(output: String) {
                outputToConsoleFunction(output)
            }
        })
    }
)
