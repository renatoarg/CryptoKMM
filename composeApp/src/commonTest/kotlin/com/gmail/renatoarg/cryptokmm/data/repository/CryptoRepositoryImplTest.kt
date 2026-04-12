package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.remote.COINGECKO_BASE_URL
import com.gmail.renatoarg.cryptokmm.data.remote.CryptoRemoteDatasource
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

/** Unit tests for [CryptoRepositoryImpl] verifying correct delegation to the datasource. */
class CryptoRepositoryImplTest {

    private val sampleJson = """
        [
          {
            "id": "bitcoin",
            "symbol": "btc",
            "name": "Bitcoin",
            "image": "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
            "current_price": 67000.0,
            "market_cap": 1300000000000,
            "market_cap_rank": 1,
            "price_change_percentage_24h": 2.5
          }
        ]
    """.trimIndent()

    /** Creates a [CryptoRepositoryImpl] backed by a [MockEngine] returning [responseJson]. */
    private fun createRepository(responseJson: String): CryptoRepositoryImpl {
        val mockEngine = MockEngine { request ->
            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url(COINGECKO_BASE_URL)
            }
        }
        val datasource = CryptoRemoteDatasource(httpClient)
        return CryptoRepositoryImpl(datasource)
    }

    @Test
    fun fetchCryptoPrices_delegatesToDatasource() = runTest {
        val repository = createRepository(sampleJson)

        val result = repository.fetchCryptoPrices()

        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("btc", result[0].symbol)
        assertEquals(67000.0, result[0].currentPrice)
    }

    @Test
    fun fetchCryptoPrices_emptyList() = runTest {
        val repository = createRepository("[]")

        val result = repository.fetchCryptoPrices()

        assertEquals(0, result.size)
    }
}
