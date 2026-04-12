package com.gmail.renatoarg.cryptokmm.data.remote

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
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Unit tests for [CryptoRemoteDatasource] using Ktor [MockEngine]. */
class CryptoRemoteDatasourceTest {

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
          },
          {
            "id": "ethereum",
            "symbol": "eth",
            "name": "Ethereum",
            "image": "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
            "current_price": 3500.0,
            "market_cap": 420000000000,
            "market_cap_rank": 2,
            "price_change_percentage_24h": null
          }
        ]
    """.trimIndent()

    /**
     * Creates a mock [HttpClient] mimicking the real client setup (base URL + optional API key).
     * The [onRequest] callback receives each request for assertion.
     */
    private fun createMockClient(
        responseJson: String,
        apiKey: String = "",
        onRequest: (io.ktor.client.request.HttpRequestData) -> Unit = {},
    ): HttpClient {
        val mockEngine = MockEngine { request ->
            onRequest(request)
            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url(COINGECKO_BASE_URL)
                if (apiKey.isNotBlank()) {
                    url.parameters.append(API_KEY_QUERY_PARAM, apiKey)
                }
            }
        }
    }

    @Test
    fun fetchCryptoPrices_parsesResponseCorrectly() = runTest {
        val datasource = CryptoRemoteDatasource(createMockClient(sampleJson))

        val result = datasource.fetchCryptoPrices()

        assertEquals(2, result.size)

        val bitcoin = result[0]
        assertEquals("bitcoin", bitcoin.id)
        assertEquals("btc", bitcoin.symbol)
        assertEquals("Bitcoin", bitcoin.name)
        assertEquals(67000.0, bitcoin.currentPrice)
        assertEquals(1300000000000L, bitcoin.marketCap)
        assertEquals(1, bitcoin.marketCapRank)
        assertEquals(2.5, bitcoin.priceChangePercentage24h)

        val ethereum = result[1]
        assertEquals("ethereum", ethereum.id)
        assertEquals("eth", ethereum.symbol)
        assertNull(ethereum.priceChangePercentage24h)
    }

    @Test
    fun fetchCryptoPrices_sendsCorrectQueryParameters() = runTest {
        var capturedUrl = ""
        val client = createMockClient("[]") { request -> capturedUrl = request.url.toString() }
        val datasource = CryptoRemoteDatasource(client)

        datasource.fetchCryptoPrices(vsCurrency = "eur", perPage = 10, page = 2)

        assertTrue(capturedUrl.contains("vs_currency=eur"), "Expected vs_currency=eur in: $capturedUrl")
        assertTrue(capturedUrl.contains("per_page=10"), "Expected per_page=10 in: $capturedUrl")
        assertTrue(capturedUrl.contains("page=2"), "Expected page=2 in: $capturedUrl")
    }

    @Test
    fun fetchCryptoPrices_usesCorrectEndpoint() = runTest {
        var capturedUrl = ""
        val client = createMockClient("[]") { request -> capturedUrl = request.url.toString() }
        val datasource = CryptoRemoteDatasource(client)

        datasource.fetchCryptoPrices()

        assertTrue(
            capturedUrl.contains("${COINGECKO_BASE_URL}coins/markets"),
            "Expected base URL + coins/markets in: $capturedUrl",
        )
    }

    @Test
    fun fetchCryptoPrices_includesApiKeyAsQueryParam() = runTest {
        var capturedUrl = ""
        val client = createMockClient("[]", apiKey = "test-key") { request ->
            capturedUrl = request.url.toString()
        }
        val datasource = CryptoRemoteDatasource(client)

        datasource.fetchCryptoPrices()

        assertTrue(
            capturedUrl.contains("$API_KEY_QUERY_PARAM=test-key"),
            "Expected API key query param in: $capturedUrl",
        )
    }

    @Test
    fun fetchCryptoPrices_omitsApiKeyWhenEmpty() = runTest {
        var capturedUrl = ""
        val client = createMockClient("[]", apiKey = "") { request ->
            capturedUrl = request.url.toString()
        }
        val datasource = CryptoRemoteDatasource(client)

        datasource.fetchCryptoPrices()

        assertFalse(
            capturedUrl.contains(API_KEY_QUERY_PARAM),
            "API key param should be absent in: $capturedUrl",
        )
    }

    @Test
    fun fetchCryptoPrices_emptyResponse() = runTest {
        val datasource = CryptoRemoteDatasource(createMockClient("[]"))

        val result = datasource.fetchCryptoPrices()

        assertTrue(result.isEmpty())
    }
}
