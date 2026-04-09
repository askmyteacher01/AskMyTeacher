package com.askmyteacher.app.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun QuestionDetailScreen(
    questionId: String,
    onBack: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    val viewModel: DetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailViewModel(context) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(questionId) {
        viewModel.loadQuestion(questionId)
    }

    QuestionDetailContent(
        state = state,
        onBack = onBack,
        onDeleteClick = {
            viewModel.deleteQuestion(questionId) {
                onBack()
            }
        }
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
            onBack = {},
            onDeleteClick = {}
        )
    }
}