package com.askmyteacher.app.presentation.ask

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AskDoubtContent(
    state: AskDoubtUiState,
    onQuestionChange: (String) -> Unit,
    onImageClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onBack: () -> Unit
) {

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Ask a Doubt",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = state.questionText,
                onValueChange = onQuestionChange,
                label = { Text("Enter your question") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { onImageClick() }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                if (state.selectedImageUri != null) {
                    AsyncImage(
                        model = state.selectedImageUri,
                        contentDescription = "Captured Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Tap to attach image (Camera)")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSubmitClick,
                enabled = state.canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Question")
            }
        }
    }
}
