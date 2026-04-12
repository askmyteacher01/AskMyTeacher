package com.askmyteacher.app.presentation.ask

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.askmyteacher.app.presentation.component.AskMyTeacherTopAppBar
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme
import com.askmyteacher.app.utils.isNetworkAvailable
import java.io.File

@Composable
fun AskDoubtScreen(
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val viewModel: AskDoubtViewModel = viewModel(
        factory = AskDoubtViewModelFactory(context)
    )
    val state by viewModel.uiState.collectAsState()

    val imageFile = remember {
        File(context.cacheDir, "captured_image.jpg")
    }

    val imageUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onSubmit()
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.setImage(imageUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(imageUri)
        }
    }

    Scaffold(
        topBar = {
            AskMyTeacherTopAppBar(
                showBack = true,
                showSettings = false,
                onBackClick = onBack
            )
        }
    ) { padding ->

        AskDoubtContent(
            modifier = Modifier.padding(padding),
            state = state,
            onQuestionChange = viewModel::onQuestionChange,
            onImageClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        cameraLauncher.launch(imageUri)
                    }
                    else -> {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            onSubmitClick = {

                val isOnline = isNetworkAvailable(context)

                viewModel.submitQuestion(
                    imageFile = if (state.selectedImageUri != null) imageFile else null,
                    isOnline = isOnline
                )
            }
        )
    }
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
        )
    }
}
