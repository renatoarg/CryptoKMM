package com.gmail.renatoarg.cryptokmm.data.repository

import com.gmail.renatoarg.cryptokmm.data.local.CryptoLocalDatasource
import com.gmail.renatoarg.cryptokmm.data.local.FakeCryptoCoinDao
import com.gmail.renatoarg.cryptokmm.data.local.entity.CryptoCoinEntity
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
import kotlin.test.assertTrue

/** Unit tests for [CryptoRepositoryImpl] covering remote delegation and local cache logic. */
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

    private fun createSuccessClient(responseJson: String): HttpClient {
        val mockEngine = MockEngine {
            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            defaultRequest { url(COINGECKO_BASE_URL) }
        }
    }

    private fun createFailingClient(): HttpClient {
        val mockEngine = MockEngine { error("Network error") }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            defaultRequest { url(COINGECKO_BASE_URL) }
        }
    }

    private fun createRepository(
        client: HttpClient,
        dao: FakeCryptoCoinDao = FakeCryptoCoinDao(),
    ): Pair<CryptoRepositoryImpl, FakeCryptoCoinDao> {
        val remote = CryptoRemoteDatasource(client)
        val local = CryptoLocalDatasource(dao)
        return CryptoRepositoryImpl(remote, local) to dao
    }

    // region Remote success

    @Test
    fun fetchCryptoPrices_remoteSuccess_returnsMappedData() = runTest {
        val (repository, _) = createRepository(createSuccessClient(sampleJson))

        val result = repository.fetchCryptoPrices()

        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals("btc", result[0].symbol)
        assertEquals(67000.0, result[0].currentPrice)
    }

    @Test
    fun fetchCryptoPrices_remoteSuccess_cachesDataLocally() = runTest {
        val (repository, dao) = createRepository(createSuccessClient(sampleJson))

        repository.fetchCryptoPrices()

        val cached = dao.getAll()
        assertEquals(1, cached.size)
        assertEquals("bitcoin", cached[0].id)
        assertEquals(67000.0, cached[0].currentPrice)
    }

    @Test
    fun fetchCryptoPrices_remoteSuccess_replacesOldCache() = runTest {
        val dao = FakeCryptoCoinDao()
        dao.insertAll(
            listOf(CryptoCoinEntity("old", "old", "OldCoin", "img", 1.0, 100, null, null))
        )
        val (repository, _) = createRepository(createSuccessClient(sampleJson), dao)

        repository.fetchCryptoPrices()

        val cached = dao.getAll()
        assertEquals(1, cached.size)
        assertEquals("bitcoin", cached[0].id)
    }

    @Test
    fun fetchCryptoPrices_emptyRemoteResponse() = runTest {
        val (repository, _) = createRepository(createSuccessClient("[]"))

        val result = repository.fetchCryptoPrices()

        assertTrue(result.isEmpty())
    }

    // endregion

    // region Remote failure — local fallback

    @Test
    fun fetchCryptoPrices_remoteFailure_returnsCachedData() = runTest {
        val dao = FakeCryptoCoinDao()
        dao.insertAll(
            listOf(CryptoCoinEntity("bitcoin", "btc", "Bitcoin", "img", 65000.0, 1_200_000_000_000, 1, -0.5))
        )
        val (repository, _) = createRepository(createFailingClient(), dao)

        val result = repository.fetchCryptoPrices()

        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals(65000.0, result[0].currentPrice)
    }

    @Test
    fun fetchCryptoPrices_remoteFailure_emptyCache_returnsEmptyList() = runTest {
        val (repository, _) = createRepository(createFailingClient())

        val result = repository.fetchCryptoPrices()

        assertTrue(result.isEmpty())
    }

    @Test
    fun fetchCryptoPrices_remoteFailure_cacheIsNotModified() = runTest {
        val dao = FakeCryptoCoinDao()
        dao.insertAll(
            listOf(CryptoCoinEntity("bitcoin", "btc", "Bitcoin", "img", 65000.0, 1_200_000_000_000, 1, -0.5))
        )
        val (repository, _) = createRepository(createFailingClient(), dao)

        repository.fetchCryptoPrices()

        val cached = dao.getAll()
        assertEquals(1, cached.size)
        assertEquals(65000.0, cached[0].currentPrice)
    }

    // endregion

    // region Local save failure

    @Test
    fun fetchCryptoPrices_remoteSuccess_localSaveFails_stillReturnsRemoteData() = runTest {
        val dao = FakeCryptoCoinDao().apply { shouldFailOnWrite = true }
        val (repository, _) = createRepository(createSuccessClient(sampleJson), dao)

        val result = repository.fetchCryptoPrices()

        assertEquals(1, result.size)
        assertEquals("bitcoin", result[0].id)
        assertEquals(67000.0, result[0].currentPrice)
    }

    // endregion
}
