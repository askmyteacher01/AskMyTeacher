package com.askmyteacher.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.askmyteacher.app.data.model.Question

@Composable
fun HomeContent(
    state: HomeUiState,
    onQuestionClick: (Question) -> Unit,
    onAskDoubtClick: () -> Unit,
    onDismissPreview: () -> Unit,
    onOpenDetail: (Question) -> Unit,
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAskDoubtClick) {
                Text("+")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when {

                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    Text(
                        text = state.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.questions.isEmpty() -> {
                    Text(
                        text = "No questions yet.\nTap + to ask one.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
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
    }
    state.previewQuestion?.let { question ->

        AlertDialog(
            onDismissRequest = onDismissPreview,
            confirmButton = {
                TextButton(
                    onClick = { onOpenDetail(question) }
                ) {
                    Text("Open Details")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissPreview) {
                    Text("Close")
                }
            },
            title = { Text("Question Preview") },
            text = {
                Column {
                    Text(question.questionText)
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusBadge(status = question.status)
                }
            }
        )
    }
}