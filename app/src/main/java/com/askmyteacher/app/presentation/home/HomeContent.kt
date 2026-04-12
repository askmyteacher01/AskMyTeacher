package com.askmyteacher.app.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.askmyteacher.app.data.model.Question
import com.askmyteacher.app.ui.theme.DarkBackground

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onQuestionClick: (Question) -> Unit,
    onAskDoubtClick: () -> Unit,
    onDismissPreview: () -> Unit,
    onOpenDetail: (Question) -> Unit,
) {

    val isDark = MaterialTheme.colorScheme.background == DarkBackground

    val gradient = if (isDark) {
        Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.background
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.background
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        when {

            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            state.error != null -> {
                Text(
                    text = state.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.questions.isEmpty() -> {
                Text(
                    text = "No questions yet.\nTap + to ask one.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.questions) { question ->
                        QuestionCard(
                            question = question,
                            onClick = { onQuestionClick(question) }
                        )
                    }
                }
            }
        }
    }

    state.previewQuestion?.let { question ->

        AlertDialog(
            onDismissRequest = onDismissPreview,
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    text = "Question Preview",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    ) {
                        Text(
                            text = question.questionText,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    StatusBadge(status = question.status)
                }
            },
            confirmButton = {
                Button(
                    onClick = { onOpenDetail(question) },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Open Details")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissPreview) {
                    Text("Close")
                }
            }
        )
    }
}