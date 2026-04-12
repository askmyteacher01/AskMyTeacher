package com.askmyteacher.app.presentation.ask

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AskDoubtContent(
    modifier: Modifier = Modifier,
    state: AskDoubtUiState,
    onQuestionChange: (String) -> Unit,
    onImageClick: () -> Unit,
    onSubmitClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Ask a Doubt",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Describe your problem clearly or attach an image.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable { onImageClick() },
            shape = RoundedCornerShape(22.dp),
            tonalElevation = 2.dp,
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) {

            Box(contentAlignment = Alignment.Center) {

                if (state.selectedImageUri != null) {

                    AsyncImage(
                        model = state.selectedImageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )

                } else {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )

                        Text(
                            text = "Tap to attach image",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = state.questionText,
            onValueChange = onQuestionChange,
            label = { Text("Enter your question") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            minLines = 5
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSubmitClick,
            enabled = state.canSubmit && !state.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50)
        ) {

            if (state.isSubmitting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = "Submit Question",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}