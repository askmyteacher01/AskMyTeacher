package com.askmyteacher.app.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

@Composable
fun SettingsScreen(
    onLogoutSuccess: () -> Unit
) {

    val context = LocalContext.current
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(context)
    )

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Local Cache")
            }

            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearCache {
                            showClearDialog = false
                        }
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false }
                ) { Text("Cancel") }
            },
            title = { Text("Clear Cache?") },
            text = { Text("This will remove all locally stored questions.") }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.logout {
                            showLogoutDialog = false
                            onLogoutSuccess()
                        }
                    }
                ) { Text("Logout") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) { Text("Cancel") }
            },
            title = { Text("Logout?") },
            text = { Text("You will be redirected to the login screen.") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {

    AskMyTeacherTheme {
        SettingsScreen(
            onLogoutSuccess = {}
        )
    }
}
