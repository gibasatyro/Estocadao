package com.fatec.estocadao.plugins

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

fun createAppSupabaseClient(url: String, key: String): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = url,
        supabaseKey = key
    ) {
        install(Postgrest)
    }
}