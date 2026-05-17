package com.fatec.estocadao.routes

import com.fatec.estocadao.domain.models.StockItem
import com.fatec.estocadao.domain.models.StockSummary
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.stockRoutes(supabase: SupabaseClient) {
    route("/stock") {

        // ENDPOINT ESPECIAL: RESUMO (Deve vir antes do /{id} para o Ktor não confundir "summary" com um ID)
        get("/summary") {
            try {
                // Lê diretamente da View que criamos no Supabase
                val summary = supabase.postgrest["stock_summary"].select().decodeList<StockSummary>()
                call.respond(summary)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro no resumo")))
            }
        }

        // 1. LISTAR TODOS
        get {
            try {
                val items = supabase.postgrest["stock_items"].select().decodeList<StockItem>()
                call.respond(items)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro desconhecido")))
            }
        }

        // 2. BUSCAR POR ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                val item = supabase.postgrest["stock_items"].select {
                    filter { eq("id", id) }
                }.decodeSingleOrNull<StockItem>()

                if (item != null) call.respond(item) else call.respond(HttpStatusCode.NotFound, "Item não encontrado")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro ao buscar")))
            }
        }

        // 3. ADICIONAR AO ESTOQUE (POST)
        post {
            try {
                val item = call.receive<StockItem>()
                supabase.postgrest["stock_items"].insert(item)
                call.respond(HttpStatusCode.Created, item)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Erro de serialização")))
            }
        }

        // 4. ATUALIZAR ITEM (PUT)
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                val updatedItem = call.receive<StockItem>()
                supabase.postgrest["stock_items"].update(updatedItem) {
                    filter { eq("id", id) }
                }
                call.respond(HttpStatusCode.OK, updatedItem)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Erro ao atualizar")))
            }
        }

        // 5. REMOVER ITEM (DELETE)
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                supabase.postgrest["stock_items"].delete {
                    filter { eq("id", id) }
                }
                call.respond(HttpStatusCode.NoContent)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro ao deletar")))
            }
        }
    }
}

