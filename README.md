# CryptoKMM

Kotlin Multiplatform (KMP) app that displays real-time cryptocurrency prices. Targets **Android** and **iOS** with shared business logic and UI via Compose Multiplatform.

## API

This app uses the [CoinGecko API](https://www.coingecko.com/en/api) (`/api/v3/coins/markets`) to fetch cryptocurrency market data (price, market cap, 24h change).

### Adding the API key

CoinGecko's free Demo API works without a key but has strict rate limits. To use a key:

1. Create a free account at [coingecko.com](https://www.coingecko.com/en/api) and generate a **Demo API key**.
2. Open `gradle.properties` in the project root and set:
   ```properties
   API_KEY=your_key_here
   ```
3. The key is read at build time via `BuildConfig.API_KEY` (Android) and injected as the `x_cg_demo_api_key` query parameter on every request.

> **Do not commit `gradle.properties` with your key.** The file in the repository keeps `API_KEY=` empty.

## Architecture

Clean Architecture with three layers, all in shared code (`commonMain`):

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Presentation │ ──▶ │    Domain    │ ◀── │     Data     │
│  (ViewModel  │     │  (UseCase,   │     │ (Repository, │
│   + Compose) │     │   Model)     │     │  Datasources)│
└──────────────┘     └──────────────┘     └──────────────┘
```

### Data flow

1. **App launch** — ViewModel loads cached data from Room instantly, then refreshes from the API in the background.
2. **Remote** — `CryptoRemoteDatasource` calls CoinGecko via Ktor and returns `List<CryptoCoinDto>`.
3. **Mapping** — DTOs are mapped to domain models (`CryptoCoin`) and to Room entities (`CryptoCoinEntity`) via dedicated mappers.
4. **Cache** — `CryptoLocalDatasource` persists entities in a Room database with an atomic `replaceAll()` transaction.
5. **UI** — `CryptoListViewModel` exposes `StateFlow<CryptoListUiState>` collected by Compose.

### Connectivity monitoring

A reactive `ConnectivityMonitor` interface exposes `isConnected: StateFlow<Boolean>`:

| Platform | Implementation |
|----------|---------------|
| Android  | `ConnectivityManager.registerDefaultNetworkCallback` |
| iOS      | `nw_path_monitor` (Network.framework C API) |

When the device goes offline an **indefinite Snackbar** appears automatically; it dismisses when connectivity is restored.

## Tech stack

| Category | Library | Version |
|----------|---------|---------|
| Language | Kotlin Multiplatform | 2.3.20 |
| UI | Compose Multiplatform + Material 3 | 1.10.3 |
| Networking | Ktor | 3.4.2 |
| Serialization | kotlinx.serialization | 1.11.0 |
| Local DB | Room (Multiplatform) + BundledSQLite | 2.7.1 |
| DI | Koin | 4.2.0 |
| Async | Coroutines | 1.10.2 |

**Android:** minSdk 24 · targetSdk 36 · JVM 17
**iOS:** arm64, x64, simulatorArm64

## Project structure

```
composeApp/src/
├── commonMain/        Shared code (domain, data, DI, UI)
├── commonTest/        Unit tests (datasources, mappers, repository)
├── androidMain/       Android-specific (Room driver, ConnectivityManager, BuildConfig)
└── iosMain/           iOS-specific (Room driver, NWPathMonitor, NSFileManager)
```

Platform-specific code uses the **expect/actual** pattern:
- `File.kt` (expect in commonMain)
- `File.android.kt` / `File.ios.kt` (actual implementations)

## Build & run

### Android

```shell
./gradlew :composeApp:assembleDebug
```

Or use the run configuration in Android Studio / Fleet.

### iOS

Open `iosApp/` in Xcode and run, or use the KMP run configuration in your IDE.

### Tests

```shell
./gradlew :composeApp:allTests
```

Runs unit tests on Android JVM, iOS Simulator (arm64), and iOS x64 targets.
