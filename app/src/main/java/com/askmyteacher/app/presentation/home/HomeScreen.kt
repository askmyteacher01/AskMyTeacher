package com.askmyteacher.app.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askmyteacher.app.presentation.component.AskMyTeacherTopAppBar
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit = {},
    onAskDoubtClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(context) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Scaffold(
        topBar = {
            AskMyTeacherTopAppBar(
                onSettingsClick = onSettingsClick
            )
        },
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(8.dp),
                onClick = onAskDoubtClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "+",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 36.sp,
                )
            }
        }
    ) { padding ->

        HomeContent(
            modifier = Modifier.padding(padding),
            state = state,
            onQuestionClick = { question ->
                viewModel.openPreview(question)
            },
            onDismissPreview = {
                viewModel.closePreview()
            },
            onOpenDetail = { question ->
                viewModel.closePreview()
                onOpenDetail(question.id ?: "")
            },
            onAskDoubtClick = onAskDoubtClick
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val sampleQuestions = listOf(
        com.askmyteacher.app.data.model.Question(
            id = "1",
            userId = "demo",
            questionText = "What is Ohm's Law?",
            status = "Answered"
        ),
        com.askmyteacher.app.data.model.Question(
            id = "2",
            userId = "demo",
            questionText = "Explain Newton's Second Law of Motion",
            status = "Pending"
        )
    )

    AskMyTeacherTheme {
        HomeContent(
            state = HomeUiState(
                questions = sampleQuestions,
                isLoading = false,
                error = null
            ),
            onQuestionClick = {},
            onAskDoubtClick = {},
            onDismissPreview = {},
            onOpenDetail = {}
        )
    }
}
