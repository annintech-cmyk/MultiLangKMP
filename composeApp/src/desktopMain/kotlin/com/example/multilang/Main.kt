package com.example.multilang

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MultiLang Login",
        state = WindowState(width = 480.dp, height = 720.dp),
    ) {
        App()
    }
}
