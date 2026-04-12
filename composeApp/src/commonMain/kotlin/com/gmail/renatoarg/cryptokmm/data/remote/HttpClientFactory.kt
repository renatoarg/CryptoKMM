package com.gmail.renatoarg.cryptokmm.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** CoinGecko API base URL (with trailing slash for relative path resolution). */
internal const val COINGECKO_BASE_URL = "https://api.coingecko.com/api/v3/"

/** CoinGecko demo API key query parameter name. */
internal const val API_KEY_QUERY_PARAM = "x_cg_demo_api_key"

/** Connect and socket timeout in milliseconds (matches OkHttp 30s default). */
private const val TIMEOUT_MS = 30_000L

/**
 * Creates a platform-agnostic [HttpClient] configured for the CoinGecko API.
 *
 * Mirrors the OkHttp/Retrofit `ApiClient` setup:
 * - JSON content negotiation (kotlinx.serialization)
 * - HTTP logging (ALL in debug, NONE in release)
 * - 30-second connect and read timeouts
 * - Default base URL with API key query parameter on every request
 *
 * The engine is resolved automatically per platform (OkHttp on Android, Darwin on iOS).
 *
 * @param apiKey CoinGecko demo API key. When not blank, appended as `x_cg_demo_api_key` query param.
 * @param isDebug Enables verbose HTTP logging when true.
 */
fun createHttpClient(apiKey: String = "", isDebug: Boolean = false): HttpClient {
    return HttpClient {
        // JSON serialization
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        // HTTP logging — ALL (headers + body) in debug, NONE in release
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HttpClient: $message")
                }
            }
            level = if (isDebug) LogLevel.ALL else LogLevel.NONE
        }
        // Timeouts — 30s connect + 30s read
        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT_MS
            socketTimeoutMillis = TIMEOUT_MS
        }
        // Base URL + API key query parameter (equivalent to OkHttp interceptor)
        defaultRequest {
            url(COINGECKO_BASE_URL)
            if (apiKey.isNotBlank()) {
                url.parameters.append(API_KEY_QUERY_PARAM, apiKey)
            }
        }
    }
}
