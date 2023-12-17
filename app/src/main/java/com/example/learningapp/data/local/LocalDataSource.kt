package com.example.learningapp.data.local

import com.example.learningapp.data.model.Question
import com.example.learningapp.data.model.Subject

interface LocalDataSource {
    val subjects: List<Subject>
    val questions: Map<Subject, List<Question>>
}
