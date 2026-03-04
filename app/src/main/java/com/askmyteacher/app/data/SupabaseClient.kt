package com.askmyteacher.app.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseManager {

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://cibvegjyoxmbfrfeicrx.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNpYnZlZ2p5b3htYmZyZmVpY3J4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzEyOTk4MTcsImV4cCI6MjA4Njg3NTgxN30.G94nH59sycW_IkrB97TxrCf7Ig4se6SYw6W_5C8Un28"
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
    }
}
