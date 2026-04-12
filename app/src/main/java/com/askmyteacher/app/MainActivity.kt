package com.askmyteacher.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.askmyteacher.app.navigation.AskMyTeacherNavGraph
import com.askmyteacher.app.ui.theme.AskMyTeacherTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AskMyTeacherTheme {
                AskMyTeacherNavGraph()
            }
        }
    }
}