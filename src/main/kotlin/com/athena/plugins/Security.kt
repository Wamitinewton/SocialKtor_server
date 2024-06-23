package com.athena.plugins

import com.athena.model.AuthResponse
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.Date


private val jwtAudience = System.getenv("jwt-audience")
private val jwtDomain = System.getenv("jwt.domain")
private val jwtRealm = System.getenv("jwt.realm")
private val jwtSecret = System.getenv("jwt.secret")

private const val CLAIM = "email"
fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain

    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(CLAIM).asString() != null){
                    JWTPrincipal(payload = credential.payload)
                } else{
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponse(
                        errorMessage = "Token is invalid or has expired"
                    )
                )
            }
        }
    }
}

fun generateToken(email: String) : String{
    val expiritionDate = Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim(CLAIM, email)
        .withExpiresAt(expiritionDate)
        .sign(Algorithm.HMAC256(jwtSecret))
}
