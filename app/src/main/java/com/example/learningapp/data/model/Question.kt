package com.example.learningapp.data.model

data class Question(
    val questionText: String,
    val variants: List<String>,
    val correctAnswerIndex: Int,
)
