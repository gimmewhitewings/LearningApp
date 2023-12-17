package com.example.learningapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.learningapp.data.model.Subject
import com.example.learningapp.ui.Screen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenLayout(
        subjectsList = uiState.subjectsList,
        onSubjectClick = { subject ->
            navController.navigate("${Screen.Test.route}/${subject.name}")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenLayout(
    subjectsList: List<Subject> = Subject.entries,
    onSubjectClick: (Subject) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                subjectsList.forEach { subject ->
                    ElevatedButton(
                        onClick = { onSubjectClick(subject) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = subject.nameResourceId))
                    }
                }
            }
        }
    }
}