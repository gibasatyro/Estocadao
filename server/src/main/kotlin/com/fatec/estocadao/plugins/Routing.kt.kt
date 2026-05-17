package com.fatec.estocadao.plugins

import com.fatec.estocadao.routes.productRoutes
import com.fatec.estocadao.routes.stockRoutes
import io.github.jan.supabase.SupabaseClient
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(supabase: SupabaseClient?) {
    routing {
        get("/") {
            call.respondText("Estocadao API is running!")
        }

        get("/api/health") {
            call.respondText("""{"status":"ok"}""", io.ktor.http.ContentType.Application.Json)
        }

        if (supabase != null) {
            productRoutes(supabase)
            stockRoutes(supabase)
        } else {
            route("/api") {
                get("{...}") {
                    call.respondText("SupabaseClient is null. Check local.properties.", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}

