package com.askmyteacher.app.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.askmyteacher.app.data.model.Question
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestionCard(
    question: Question,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            question.createdAt?.let {
                Text(
                    text = formatDate(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusBadge(status = question.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {

    val color = if (status == "Answered")
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.tertiary

    AssistChip(
        onClick = {},
        label = { Text(status) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String {

    if (dateString == null) return ""

    return try {
        val dateTime = OffsetDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy • hh:mm a")
        dateTime.atZoneSameInstant(ZoneId.systemDefault()).format(formatter)
    } catch (e: Exception) {
        ""
    }
}