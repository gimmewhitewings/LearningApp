package com.example.learningapp.ui.screens.test

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningapp.SUBJECT_NAME_ARGUMENT
import com.example.learningapp.data.LearningRepository
import com.example.learningapp.data.model.Question
import com.example.learningapp.data.model.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: LearningRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val subjectNameParameter: String =
        savedStateHandle[SUBJECT_NAME_ARGUMENT] ?: Subject.MATH.name

    private val _uiState = MutableStateFlow(TestUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            setSubjectFromParameter()
            setQuestion()
        }
    }

    private fun setSubjectFromParameter() {
        _uiState.update {
            it.copy(
                currentSubject = repository.subjects.first { subject ->
                    subject.name == subjectNameParameter
                }
            )
        }
    }

    private fun setQuestion() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    question = repository.getQuestionOfSubjectAtIndex(
                        it.currentSubject,
                        it.questionNumber
                    )
                )
            }
        }
    }

    fun nextQuestion() {
        increaseQuestionNumber()
        setQuestion()
    }

    private fun increaseQuestionNumber() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(questionNumber = it.questionNumber + 1)
            }
        }
    }

    fun selectAnswer(index: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(userAnswerIndex = index)
            }
        }
    }

    fun submitAnswer() {
        TODO("Not yet implemented")
    }

    fun skipQuestion() {
        TODO("Not yet implemented")
    }

    fun resetGame() {
        TODO("Not yet implemented")
    }
}

data class TestUiState(
    val currentSubject: Subject = Subject.MATH,
    val currentScore: Int = 0,
    val question: Question? = null,
    val questionNumber: Int = 0,
    val userAnswerIndex: Int? = null,
    val isGameOver: Boolean = false
)
