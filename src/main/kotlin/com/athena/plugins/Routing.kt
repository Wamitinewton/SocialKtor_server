package com.athena.plugins

import com.athena.route.authRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
       authRouting()
    }
}
