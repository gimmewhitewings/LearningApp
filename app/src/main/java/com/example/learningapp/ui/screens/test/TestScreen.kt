package com.example.learningapp.ui.screens.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningapp.R
import com.example.learningapp.data.model.Question
import com.example.learningapp.data.model.Subject

@Composable
fun TestScreen(
    viewModel: TestViewModel,
    navController: NavController,
) {
    val uiState by viewModel.uiState.collectAsState()
    TestLayout(
        subject = uiState.currentSubject,
        onNavigationIconClick = { navController.popBackStack() },
        question = uiState.question,
        userAnswerIndex = uiState.userAnswerIndex,
        selectAnswer = viewModel::selectAnswer,
        submitAnswer = viewModel::submitAnswer,
        skipQuestion = viewModel::skipQuestion,
        scores = uiState.currentScore,
        isGameOver = uiState.isGameOver,
        resetGame = viewModel::resetGame
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TestLayout(
    subject: Subject = Subject.INFORMATICS,
    onNavigationIconClick: () -> Unit = {},
    question: Question? = Question(
        "Что такое ООП в программировании?",
        listOf(
            "Объектно-ориентированное программирование",
            "Операционная система",
            "Очень общий протокол",
            "Объектный опыт программиста"
        ),
        0
    ),
    userAnswerIndex: Int? = null,
    selectAnswer: (Int) -> Unit = {},
    submitAnswer: () -> Unit = {},
    skipQuestion: () -> Unit = {},
    scores: Int = 0,
    isGameOver: Boolean = false,
    resetGame: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = stringResource(id = subject.nameResourceId)) },
            navigationIcon = {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, null)
                }
            }
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (question != null) {
                ScoreBoard(
                    modifier = Modifier.align(Alignment.TopCenter),
                    scores = scores
                )
                GameCard(
                    question = question,
                    userAnswerIndex = userAnswerIndex,
                    selectAnswer = selectAnswer,
                    submitAnswer = submitAnswer,
                    skipQuestion = skipQuestion
                )
                Button(
                    onClick = resetGame,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, null)
                }
            } else {
                CircularProgressIndicator()
            }
        }
        if (isGameOver) {
            GameOverDialog(
                returnBack = onNavigationIconClick,
                resetGame = resetGame,
                scores = scores
            )
        }
    }
}

@Composable
private fun GameOverDialog(
    returnBack: () -> Unit,
    resetGame: () -> Unit,
    scores: Int,
) {
    AlertDialog(
        onDismissRequest = returnBack,
        confirmButton = {
            Button(onClick = resetGame) {
                Text(text = stringResource(id = R.string.restart))
            }
        },
        dismissButton = {
            TextButton(onClick = returnBack) {
                Text(text = stringResource(id = R.string.to_the_title_screen))
            }
        },
        title = { Text(text = stringResource(id = R.string.test_is_over)) },
        text = {
            Text(text = stringResource(id = R.string.final_score, scores))
        }
    )
}


@Composable
private fun GameCard(
    question: Question,
    userAnswerIndex: Int?,
    selectAnswer: (Int) -> Unit,
    submitAnswer: () -> Unit,
    skipQuestion: () -> Unit,
) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = question.questionText,
                style = MaterialTheme.typography.headlineSmall
            )
            question.variants.forEachIndexed { index, variant ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = index == userAnswerIndex,
                        onClick = { selectAnswer(index) }
                    )
                    Text(text = variant)
                }
            }
            Button(
                onClick = submitAnswer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.submit))
            }
            TextButton(
                onClick = skipQuestion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.skip))
            }
        }
    }
}

@Composable
private fun ScoreBoard(modifier: Modifier, scores: Int) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.score, scores),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}