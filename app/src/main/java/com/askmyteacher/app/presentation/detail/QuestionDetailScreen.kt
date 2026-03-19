package com.askmyteacher.app.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun QuestionDetailScreen(
    questionId: String,
    onBack: () -> Unit
) {

    val viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(questionId) {
        viewModel.loadQuestion(questionId)
    }

    QuestionDetailContent(
        state = state,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
fun QuestionDetailPreview() {

    val fakeQuestion = com.askmyteacher.app.data.model.Question(
        id = "1",
        userId = "demo",
        questionText = "Explain Ohm's Law in detail.",
        answerText = "Ohm's Law states that V = IR, where V is voltage, I is current, and R is resistance.",
        status = "Answered"
    )

    AskMyTeacherTheme {
        QuestionDetailContent(
            state = DetailUiState(
                question = fakeQuestion,
                isLoading = false,
                error = null
            ),
            onBack = {}
        )
    }
}