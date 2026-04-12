package com.askmyteacher.app.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.askmyteacher.app.presentation.home.StatusBadge

@Composable
fun QuestionDetailContent(
    modifier: Modifier = Modifier,
    state: DetailUiState,
    onDeleteClick: () -> Unit
) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
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
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.question != null -> {

                val question = state.question

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        tonalElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Text(
                                text = question.questionText,
                                style = MaterialTheme.typography.titleLarge
                            )

                            StatusBadge(status = question.status)
                        }
                    }

                    question.imageUrl?.let { imageUrl ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            tonalElevation = 2.dp
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(240.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        tonalElevation = 2.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = "Answer",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                style = MaterialTheme.typography.bodyLarge,
                                text = question.answerText?.let { parseMarkdown(it) }
                                    ?: AnnotatedString("Answer is being generated...")
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete Question")
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {

        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            title = {
                Text(
                    text = "Delete Question",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "Are you sure you want to permanently delete this question?",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun parseMarkdown(text: String): AnnotatedString {

    val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
    val italicRegex = Regex("\\*(.*?)\\*")

    return buildAnnotatedString {

        val lines = text.split("\n")

        lines.forEachIndexed { index, line ->

            var processedLine = line

            if (processedLine.trim().startsWith("* ")) {
                append("• ")
                processedLine = processedLine.removePrefix("* ")
            }

            var currentIndex = 0

            boldRegex.findAll(processedLine).forEach { result ->
                val start = result.range.first
                val end = result.range.last + 1

                append(processedLine.substring(currentIndex, start))

                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(result.groupValues[1])
                pop()

                currentIndex = end
            }

            val remaining = processedLine.substring(currentIndex)

            var italicIndex = 0
            italicRegex.findAll(remaining).forEach { result ->
                val start = result.range.first
                val end = result.range.last + 1

                append(remaining.substring(italicIndex, start))

                pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                append(result.groupValues[1])
                pop()

                italicIndex = end
            }

            if (italicIndex < remaining.length) {
                append(remaining.substring(italicIndex))
            }

            if (index != lines.lastIndex) {
                append("\n")
            }
        }
    }
}