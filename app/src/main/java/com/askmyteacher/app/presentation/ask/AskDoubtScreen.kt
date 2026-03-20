package com.askmyteacher.app.presentation.ask

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun AskDoubtScreen(
    onSubmit: (String) -> Unit,
    onBack: () -> Unit
) {

    var state by remember { mutableStateOf(AskDoubtUiState()) }

    AskDoubtContent(
        state = state,
        onQuestionChange = { state = state.copy(questionText = it) },
        onImageClick = { /* Commit 12 */ },
        onSubmitClick = {
            onSubmit(state.questionText)
        },
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
fun AskDoubtPreview() {

    AskMyTeacherTheme {
        AskDoubtContent(
            state = AskDoubtUiState(
                questionText = "Explain Kirchhoff’s Laws."
            ),
            onQuestionChange = {},
            onImageClick = {},
            onSubmitClick = {},
            onBack = {}
        )
    }
}
