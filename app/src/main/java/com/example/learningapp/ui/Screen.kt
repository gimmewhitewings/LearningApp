package com.example.learningapp.ui

sealed class Screen(
    open val route: String,
) {
    data object Home : Screen("home")
    data object Test : Screen("test")
}