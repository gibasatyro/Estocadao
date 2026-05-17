package com.fatec.estocadao

import com.fatec.estocadao.plugins.*
import io.github.jan.supabase.SupabaseClient
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.serialization.Serializable

fun main() {
    // Mantendo a lógica de porta flexível (ambiente ou 8080)
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@Serializable
data class HealthResponse(
    val status: String,
    val timestamp: Long
)

fun Application.module() {
    // 1. Lógica de recuperação de credenciais (Igual à aula)
    var supabaseUrl = System.getProperty("SUPABASE_URL") ?: System.getenv("SUPABASE_URL")
    var supabaseKey = System.getProperty("SUPABASE_KEY") ?: System.getenv("SUPABASE_KEY")

    if (supabaseUrl == null || supabaseKey == null) {
        val properties = java.util.Properties()
        val localPropFiles = listOf(
            java.io.File("local.properties"),
            java.io.File("../local.properties"),
            java.io.File("../../local.properties")
        )
        val file = localPropFiles.firstOrNull { it.exists() }
        if (file != null) {
            properties.load(java.io.FileInputStream(file))
            supabaseUrl = properties.getProperty("SUPABASE_URL")
            supabaseKey = properties.getProperty("SUPABASE_KEY")
        }
    }

    var supabase: SupabaseClient? = null

    if (supabaseUrl != null && supabaseKey != null) {
        // Esta função createAppSupabaseClient precisará estar em um arquivo de plugin
        supabase = createAppSupabaseClient(supabaseUrl, supabaseKey)
    }

    // 2. Configuração dos Plugins (Igual à Aula 05)
    configureSerialization()
    configureRouting(supabase)
    // Se ele ensinou CORS e StatusPages, podemos adicionar depois
}