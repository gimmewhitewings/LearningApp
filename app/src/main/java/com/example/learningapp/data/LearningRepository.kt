package com.example.learningapp.data

import com.example.learningapp.data.local.LocalDataSource
import com.example.learningapp.data.model.Question
import com.example.learningapp.data.model.Subject
import javax.inject.Inject

class LearningRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
) {
    val subjects = localDataSource.subjects

    fun getQuestionOfSubjectAtIndex(subject: Subject, index: Int): Question =
        localDataSource.questions[subject]!![index]
}
