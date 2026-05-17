package com.fatec.estocadao

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform