package com.askmyteacher.app.presentation.home

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.askmyteacher.app.data.model.Question
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun HomeScreen(
    state: HomeUiState,
    onOpenDetail: (String) -> Unit,
    onAskDoubtClick: () -> Unit
) {

    HomeContent(
        state = state,
        onQuestionClick = { question ->

        },
        onDismissPreview = {},
        onOpenDetail = { question ->
            onOpenDetail(question.id ?: "")
        },
        onAskDoubtClick = onAskDoubtClick
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

    val sampleQuestions = listOf(
        Question(
            id = "1",
            userId = "demo",
            questionText = "What is Ohm's Law?",
            status = "Answered"
        ),
        Question(
            id = "2",
            userId = "demo",
            questionText = "Explain Newton's Second Law of Motion",
            status = "Pending"
        )
    )

    AskMyTeacherTheme {
        HomeScreen(
            state = HomeUiState(
                questions = sampleQuestions
            ),
            onOpenDetail = {},
            onAskDoubtClick = {}
        )
    }
}