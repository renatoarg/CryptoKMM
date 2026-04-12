package com.gmail.renatoarg.cryptokmm.data.remote

import com.gmail.renatoarg.cryptokmm.data.remote.dto.CryptoCoinDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Remote datasource for CoinGecko API calls.
 *
 * Base URL, API key, timeouts and logging are configured at the [HttpClient] level
 * (see [createHttpClient]). This class only builds endpoint-specific requests.
 *
 * @param httpClient Ktor HTTP client pre-configured via [createHttpClient].
 */
internal class CryptoRemoteDatasource(private val httpClient: HttpClient) {

    /**
     * Fetches cryptocurrency market data from CoinGecko `/coins/markets`.
     *
     * @param vsCurrency Target currency (e.g. "usd", "eur").
     * @param perPage Number of results per page (max 250).
     * @param page Page number for pagination.
     * @return List of [CryptoCoinDto] with current market data.
     */
    suspend fun fetchCryptoPrices(
        vsCurrency: String = "usd",
        perPage: Int = 20,
        page: Int = 1,
    ): List<CryptoCoinDto> {
        return httpClient.get("coins/markets") {
            parameter("vs_currency", vsCurrency)
            parameter("per_page", perPage)
            parameter("page", page)
        }.body()
    }
}
