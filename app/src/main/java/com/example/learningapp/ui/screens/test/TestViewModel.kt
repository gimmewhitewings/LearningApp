package com.example.learningapp.ui.screens.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.learningapp.INITIAL_SCORE
import com.example.learningapp.QUESTION_AMOUNT
import com.example.learningapp.SCORE_FOR_ANSWER
import com.example.learningapp.SUBJECT_NAME_ARGUMENT
import com.example.learningapp.data.LearningRepository
import com.example.learningapp.data.model.Question
import com.example.learningapp.data.model.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: LearningRepository,
    savedStateHandle: SavedStateHandle? = null,
) : ViewModel() {
    val subjectNameParameter: String =
        savedStateHandle?.get(SUBJECT_NAME_ARGUMENT) ?: Subject.MATH.name

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        setSubjectFromParameter(subjectNameParameter)
        setQuestion()
    }

    private fun setSubjectFromParameter(subjectName: String) {
        _uiState.update {
            it.copy(
                currentSubject = repository.subjects.first { subject ->
                    subject.name == subjectName
                }
            )
        }
    }

    private fun setQuestion() {
        _uiState.update {
            it.copy(
                question = repository.getQuestionOfSubjectAtIndex(
                    it.currentSubject,
                    it.questionNumber
                )
            )
        }
    }

    private fun nextQuestion() {
        increaseQuestionNumber()
        if (_uiState.value.questionNumber < QUESTION_AMOUNT) {
            setQuestion()
            resetUserAnswer()
        } else {
            setTestOver()
        }
    }

    private fun setTestOver() {
        _uiState.update {
            it.copy(isTestOver = true)
        }
    }

    private fun increaseQuestionNumber() {
        _uiState.update {
            it.copy(questionNumber = it.questionNumber + 1)
        }
    }

    fun selectAnswer(index: Int) {
        _uiState.update {
            it.copy(userAnswerIndex = index)
        }
    }

    fun submitAnswer() {
        if (_uiState.value.userAnswerIndex == _uiState.value.question!!.correctAnswerIndex) {
            increaseScore()
        }
        nextQuestion()
    }

    private fun resetUserAnswer() {
        _uiState.update {
            it.copy(userAnswerIndex = null)
        }
    }

    private fun increaseScore() {
        _uiState.update {
            it.copy(currentScore = it.currentScore + SCORE_FOR_ANSWER)
        }
    }

    fun skipQuestion() {
        nextQuestion()
    }

    fun resetTest() {
        _uiState.value = TestUiState()
        setSubjectFromParameter(subjectNameParameter)
        setQuestion()
    }
}

data class TestUiState(
    val currentSubject: Subject = Subject.MATH,
    val currentScore: Int = INITIAL_SCORE,
    val question: Question? = null,
    val questionNumber: Int = 0,
    val userAnswerIndex: Int? = null,
    val isTestOver: Boolean = false,
)
