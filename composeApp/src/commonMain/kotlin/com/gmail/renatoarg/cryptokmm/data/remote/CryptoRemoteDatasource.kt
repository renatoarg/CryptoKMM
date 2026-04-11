package com.gmail.renatoarg.cryptokmm.data.remote

internal class CryptoRemoteDatasource {

    fun getCryptoImageUrls(): String {
        return """
            {
              "urls": [
                "https://assets.coingecko.com/coins/images/1/large/bitcoin.png",
                "https://assets.coingecko.com/coins/images/279/large/ethereum.png",
                "https://assets.coingecko.com/coins/images/825/large/bnb-icon2_2x.png",
                "https://assets.coingecko.com/coins/images/44/large/xrp-symbol-white-128.png",
                "https://assets.coingecko.com/coins/images/4128/large/solana.png"
              ]
            }
        """.trimIndent()
    }
}
