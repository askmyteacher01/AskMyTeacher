package com.askmyteacher.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object Splash

@Serializable
object Home

@Serializable
object Auth

@Serializable
data class Detail(val questionId: String)

@Serializable
object AskDoubt