package com.fatec.estocadao.routes

import com.fatec.estocadao.domain.models.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.productRoutes(supabase: SupabaseClient) {
    route("/products") {

        // 1. LISTAR TODOS
        get {
            try {
                val products = supabase.postgrest["products"].select().decodeList<Product>()
                call.respond(products)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro desconhecido")))
            }
        }

        // 2. BUSCAR POR ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                val product = supabase.postgrest["products"].select {
                    filter { eq("id", id) }
                }.decodeSingleOrNull<Product>()

                if (product != null) call.respond(product) else call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro ao buscar")))
            }
        }

        // 3. CADASTRAR (POST)
        post {
            try {
                val product = call.receive<Product>()
                supabase.postgrest["products"].insert(product)
                call.respond(HttpStatusCode.Created, product)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Erro de serialização")))
            }
        }

        // 4. ATUALIZAR (PUT)
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                val updatedProduct = call.receive<Product>()
                supabase.postgrest["products"].update(updatedProduct) {
                    filter { eq("id", id) }
                }
                call.respond(HttpStatusCode.OK, updatedProduct)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Erro ao atualizar")))
            }
        }

        // 5. REMOVER (DELETE)
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "ID inválido")
            try {
                supabase.postgrest["products"].delete {
                    filter { eq("id", id) }
                }
                call.respond(HttpStatusCode.NoContent)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Erro ao deletar")))
            }
        }
    }
}
