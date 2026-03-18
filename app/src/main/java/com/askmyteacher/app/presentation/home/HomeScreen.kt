package com.askmyteacher.app.presentation.home

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun HomeScreen(
    onOpenDetail: (String) -> Unit = {},
    onAskDoubtClick: () -> Unit = {}
) {

    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    HomeContent(
        state = state,
        onQuestionClick = { question ->
            onOpenDetail(question.id ?: "")
        },
        onAskDoubtClick = onAskDoubtClick
    )
}

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
            onAskDoubtClick = {}
        )
    }
}
