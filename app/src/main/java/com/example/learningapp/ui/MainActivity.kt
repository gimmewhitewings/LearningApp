package com.example.learningapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learningapp.SUBJECT_NAME_ARGUMENT
import com.example.learningapp.ui.screens.home.HomeScreen
import com.example.learningapp.ui.screens.home.HomeViewModel
import com.example.learningapp.ui.screens.test.TestScreen
import com.example.learningapp.ui.screens.test.TestViewModel
import com.example.learningapp.ui.theme.LearningAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearningAppTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            val homeViewModel = hiltViewModel<HomeViewModel>()
                            HomeScreen(
                                viewModel = homeViewModel,
                                navController = navController
                            )
                        }

                        composable(
                            route = Screen.Test.route + "/" + "{${SUBJECT_NAME_ARGUMENT}}",
                            arguments = listOf(
                                navArgument(SUBJECT_NAME_ARGUMENT) { type = NavType.StringType }
                            )
                        ) {
                            val testViewModel = hiltViewModel<TestViewModel>()
                            TestScreen(
                                viewModel = testViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

