package com.askmyteacher.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.askmyteacher.app.data.model.Question
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun HomeContent(
    state: HomeUiState,
    onQuestionClick: (Question) -> Unit,
    onDismissPreview: () -> Unit,
    onOpenDetail: (Question) -> Unit,
    onAskDoubtClick: () -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAskDoubtClick) {
                Text("+")
            }
        }
    ) { padding ->

        if (state.questions.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No questions yet. Tap + to ask one.")
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
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

    // Preview Dialog
    state.previewQuestion?.let { question ->

        AlertDialog(
            onDismissRequest = onDismissPreview,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissPreview()
                        onOpenDetail(question)
                    }
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