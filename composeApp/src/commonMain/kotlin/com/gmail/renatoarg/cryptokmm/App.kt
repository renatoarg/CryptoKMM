package com.gmail.renatoarg.cryptokmm

import kotlin.math.pow
import kotlin.math.round
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.gmail.renatoarg.cryptokmm.domain.model.CryptoCoin
import com.gmail.renatoarg.cryptokmm.presentation.CryptoListViewModel
import org.koin.compose.viewmodel.koinViewModel

private val PositiveChangeColor = Color(0xFF4CAF50)
private val NegativeChangeColor = Color(0xFFF44336)

@Composable
fun App() {
    val viewModel = koinViewModel<CryptoListViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            snackbarHostState.showSnackbar(
                message = "No Internet connection",
                duration = SnackbarDuration.Indefinite,
            )
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { innerPadding ->
            when {
                !uiState.isLoading && uiState.cryptoList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Not possible to load data")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxSize(),
                    ) {
                        items(
                            items = uiState.cryptoList,
                            key = { it.id },
                            contentType = { "crypto_row" },
                        ) { coin ->
                            CryptoRow(coin)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CryptoRow(coin: CryptoCoin) {
    val symbolText = remember(coin.symbol) { coin.symbol.uppercase() }
    val priceText = remember(coin.currentPrice) { "$${formatPrice(coin.currentPrice)}" }
    val changeText = remember(coin.priceChangePercentage24h) {
        coin.priceChangePercentage24h?.let { change ->
            val sign = if (change >= 0) "+" else ""
            "$sign${formatDecimal(change, 2)}%"
        }
    }
    val changeColor = remember(coin.priceChangePercentage24h) {
        if (coin.priceChangePercentage24h != null && coin.priceChangePercentage24h >= 0) {
            PositiveChangeColor
        } else {
            NegativeChangeColor
        }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = coin.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = symbolText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (changeText != null) {
                    Text(
                        text = changeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = changeColor,
                    )
                }
            }
        }
        HorizontalDivider()
    }
}

private fun formatPrice(price: Double): String {
    return if (price >= 1.0) {
        formatDecimal(price, 2)
    } else {
        formatDecimal(price, 6)
    }
}

private fun formatDecimal(value: Double, decimals: Int): String {
    val factor = 10.0.pow(decimals)
    val rounded = round(value * factor) / factor
    val parts = rounded.toString().split(".")
    val intPart = parts[0]
    val fracPart = if (parts.size > 1) parts[1] else ""
    return "$intPart.${fracPart.padEnd(decimals, '0').take(decimals)}"
}
