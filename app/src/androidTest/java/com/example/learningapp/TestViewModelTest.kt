package com.example.learningapp

import com.example.learningapp.data.LearningRepository
import com.example.learningapp.data.local.LocalDataSourceImpl
import com.example.learningapp.ui.screens.test.TestViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TestViewModelTest {
    private val viewModel = TestViewModel(
        LearningRepository(
            LocalDataSourceImpl()
        )
    )

    @Test
    fun initialization() {
        val testUiState = viewModel.uiState.value
        assertFalse(testUiState.isTestOver)
        assertEquals(INITIAL_SCORE, testUiState.currentScore)
        assertEquals(0, testUiState.questionNumber)
    }

    @Test
    fun correct_answer() {
        var currentTestUiState = viewModel.uiState.value
        val correctAnswerIndex = currentTestUiState.question!!.correctAnswerIndex
        val previousScore = currentTestUiState.currentScore
        val previousQuestionNumber = currentTestUiState.questionNumber
        viewModel.selectAnswer(correctAnswerIndex)
        viewModel.submitAnswer()
        currentTestUiState = viewModel.uiState.value
        assertEquals(previousScore + SCORE_FOR_ANSWER, currentTestUiState.currentScore)
        assertEquals(null, currentTestUiState.userAnswerIndex)
        assertEquals(previousQuestionNumber + 1, currentTestUiState.questionNumber)
    }

    @Test
    fun wrong_answer() {
        var currentTestUiState = viewModel.uiState.value
        val correctAnswerIndex = currentTestUiState.question!!.correctAnswerIndex
        val incorrectAnswer = listOf(0, 1, 2, 3).filter { it != correctAnswerIndex }.random()
        val previousScore = currentTestUiState.currentScore
        val previousQuestionNumber = currentTestUiState.questionNumber
        viewModel.selectAnswer(incorrectAnswer)
        viewModel.submitAnswer()
        currentTestUiState = viewModel.uiState.value
        assertEquals(previousQuestionNumber + 1, currentTestUiState.questionNumber)
        assertEquals(previousScore, currentTestUiState.currentScore)
        assertEquals(null, currentTestUiState.userAnswerIndex)
    }

    @Test
    fun questionSkipped() {
        var currentTestUiState = viewModel.uiState.value
        val previousScore = currentTestUiState.currentScore
        val previousQuestionNumber = currentTestUiState.questionNumber
        viewModel.skipQuestion()
        currentTestUiState = viewModel.uiState.value
        assertEquals(previousQuestionNumber + 1, currentTestUiState.questionNumber)
        assertEquals(previousScore, currentTestUiState.currentScore)
        assertEquals(null, currentTestUiState.userAnswerIndex)
    }

    @Test
    fun isTestOver() {
        repeat(QUESTION_AMOUNT) {
            viewModel.skipQuestion()
        }
        val currentTestUiState = viewModel.uiState.value
        assertTrue(currentTestUiState.isTestOver)
    }

    @Test
    fun restartTest() {
        viewModel.resetTest()
        val currentTestUiState = viewModel.uiState.value
        assertFalse(currentTestUiState.isTestOver)
        assertEquals(INITIAL_SCORE, currentTestUiState.currentScore)
        assertEquals(0, currentTestUiState.questionNumber)
    }
}