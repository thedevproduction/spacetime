# ⚡ Spacetime Energy Futures

**Spacetime Energy Futures** is a multi-role Android EV charging platform built with **Kotlin**, **Jetpack Compose**, **Material 3**, **Hilt DI**, and **Clean Architecture**. It serves the Indian EV mobility ecosystem from a single application across three distinct workspaces:

- **EV Customer** — Discover nearby chargers on interactive OpenStreetMap street maps, scan charger QR codes, view transparent tariff quotes, monitor live charging telemetry, pay via wallet/UPI, and manage EVs.
- **Station Owner (Site Admin)** — Monitor real-time site KPIs, gun availability, live vehicle sessions, deduction-level revenue accounting, settlement statements, and role-based staff access.
- **Service Provider** — Manage turnkey charger installations, serialized hardware inventory, SLA repair tickets, and field technician dispatches.

> For milestone tracking see [`IMPLEMENTATION_PLAN.md`](IMPLEMENTATION_PLAN.md). For cloud backend, real OTP, and Razorpay setup instructions see [`PRODUCTION_SETUP_GUIDE.md`](PRODUCTION_SETUP_GUIDE.md).

---

## 🧭 Table of Contents

1. [Architecture & Module Structure](#-architecture--module-structure)
2. [Feature Breakdown by Role](#-feature-breakdown-by-role)
3. [Detailed Feature Flows & Diagrams](#-detailed-feature-flows--diagrams)
   - [Authentication & Multi-Role Routing](#1-authentication--multi-role-routing)
   - [Station Discovery & Multi-Engine Map Engine](#2-station-discovery--multi-engine-map-engine)
   - [End-to-End Charging & Billing Sequence](#3-end-to-end-charging--billing-sequence)
   - [Charging Session State Lifecycle](#4-charging-session-state-lifecycle)
   - [Wallet Top-Up & Site Revenue Flow](#5-wallet-top-up--site-revenue-flow)
4. [Tech Stack & Design System](#-tech-stack--design-system)
5. [Running Locally & Testing](#-running-locally--testing)

---

## 🏗️ Architecture & Module Structure

The app follows a modular **MVVM + Clean Repository Architecture** where UI feature modules depend only on domain repository interfaces (`:data`) and shared core modules (`:core/*`), allowing seamless switching between in-memory demo fakes (`FakeRepositories`) and live REST APIs (`core:network` + `api/openapi.json`).

```mermaid
flowchart TB
    APP["app (MainActivity, SpacetimeNavHost, CustomerShell, SiteAdminShell, ProviderShell)"]

    subgraph AUTH["Auth & Entry"]
        FAUTH["feature-auth (Splash, Welcome, OTP, Onboarding, RoleSelection)"]
    end

    subgraph CUST["EV Customer Features"]
        FHOME["feature-customer-home"]
        FDISC["feature-station-discovery"]
        FDET["feature-station-details"]
        FQR["feature-qr-scanner"]
        FCHG["feature-charging"]
        FWAL["feature-wallet"]
        FVEH["feature-vehicles"]
        FPROF["feature-customer-profile"]
    end

    subgraph SITE["Site Admin Portal"]
        SDASH["feature-site-dashboard"]
        SSTN["feature-site-stations"]
        SSESS["feature-site-sessions"]
        SREV["feature-site-revenue"]
        SEMP["feature-site-employees"]
    end

    subgraph PROV["Service Provider Portal"]
        PDASH["feature-provider-dashboard"]
        PINST["feature-provider-installations"]
        PEQ["feature-provider-equipment"]
        PSVC["feature-provider-service"]
        PTECH["feature-provider-technicians"]
    end

    DATA["data (Domain Models, Repository Interfaces, FakeRepositories, Hilt DataModule)"]

    subgraph CORE["Core Foundation"]
        DS["core:designsystem (Material 3 Theme, SpacetimeBrandLogo, Shared Components)"]
        NAV["core:navigation (Type-Safe Routes & NavigationEvent)"]
        NET["core:network (Retrofit ApiServices, DTOs, Auth & RequestId Interceptors)"]
        SEC["core:security (TokenManager / EncryptedSharedPreferences)"]
        STORE["core:datastore (UserPreferences DataStore)"]
        COM["core:common (AppResult, Money, Constants, Role & Status Enums)"]
    end

    APP --> FAUTH
    APP --> CUST
    APP --> SITE
    APP --> PROV
    AUTH --> DATA
    CUST --> DATA
    SITE --> DATA
    PROV --> DATA
    DATA --> NET
    DATA --> SEC
    DATA --> COM
    AUTH --> DS
    CUST --> DS
    SITE --> DS
    PROV --> DS
    AUTH --> STORE
    APP --> NAV
```

### Module Inventory

| Layer | Modules | Responsibility |
|---|---|---|
| **App Shell** | `:app` | Single-activity host (`MainActivity` edge-to-edge), `SpacetimeNavHost`, and 3 bottom-navigation shells (`CustomerShell`, `SiteAdminShell`, `ProviderShell`) |
| **Core** | `:core:designsystem`, `:core:common`, `:core:navigation`, `:core:network`, `:core:security`, `:core:datastore`, `:core:testing` | Design tokens, reusable Compose components, encrypted token store, Retrofit services & interceptors, type-safe `@Serializable` routes |
| **Data** | `:data` | Domain models (`Station`, `Charger`, `ChargingQuote`, `Wallet`, `Vehicle`, etc.), 7 repository interfaces, Hilt bindings (`DataModule`), and pilot demo data (`FakeRepositories`) |
| **Customer** | `:feature-customer-home`, `:feature-station-discovery`, `:feature-station-details`, `:feature-qr-scanner`, `:feature-charging`, `:feature-wallet`, `:feature-vehicles`, `:feature-customer-profile` | Complete driver discovery, map, QR scan, quote, live charge, wallet, EV garage, and profile flows |
| **Site Admin** | `:feature-site-dashboard`, `:feature-site-stations`, `:feature-site-sessions`, `:feature-site-revenue`, `:feature-site-employees` | Site KPIs, network health, station readiness, active sessions, deductions/settlements, staff access |
| **Service Provider** | `:feature-provider-dashboard`, `:feature-provider-installations`, `:feature-provider-equipment`, `:feature-provider-service`, `:feature-provider-technicians` | Provider metrics, installation stage tracking, serialized equipment inventory, SLA tickets, technicians |

---

## 🚀 Feature Breakdown by Role

### 1. Authentication & Role Routing (`:feature-auth`)
- **Branded Splash & Auto-Resume:** Checks encrypted token status (`TokenManagerImpl`), verifies current user profile, and routes either to login or directly into the user's workspace.
- **Mobile + OTP Flow:** 10-digit Indian mobile validation (`+91`), 6-digit OTP entry with auto-submit, and a 30-second resend cooldown timer.
- **New-User Onboarding:** Captures full name, optional email, city, and terms acceptance.
- **Multi-Role Workspace Switcher:** Users holding multiple roles (e.g., `Rahul Sharma` in demo mode holds `Customer`, `Site Admin`, and `Service Provider`) choose their workspace on `RoleSelectionScreen` or switch anytime via Profile.

### 2. EV Customer Workspace (`CustomerShell`)
Bottom Navigation Tabs: **Home** • **Explore** • **Charging** • **Wallet** • **Profile**

- **Home (`CustomerHomeScreen`):** Gradient Spacetime Wallet balance card, quick-tap search bar (opens Explore), 4 quick actions (**Scan QR**, **Nearby**, **Fast DC**, **Saved**), live active-session banner if charging is running, and nearby charging hubs.
- **Explore (`ExploreMapScreen`):**
  - **4 Map Engines:** Native OpenStreetMap Street (`OsmRealMapView` MAPNIK default, no API key required), Carto Voyager (`Clean Map`), Google Maps SDK (`GoogleMapsView` with automatic fallback prompt if `MAPS_API_KEY` is empty), and an offline Canvas vector map (`NativeMapView`).
  - **Custom Station Pins & Preview:** Price + availability pins (`₹14 • 4/6`), tap-to-preview bottom card with rating, amenities, **Directions** (`geo:` intent), and **Charge Here** CTA.
  - **Search, Quick Filters & List Mode:** Instant search dropdown, quick chips (`All`, `Fast DC`, `Available Now`, `24/7 Open`, `Lowest Tariff`), full filter bottom sheet (category, max distance slider `5–50 km`, 24/7 toggle), and a List View with 4 sort modes (`Nearest`, `Lowest Price`, `Fastest`, `Top Rated`).
  - **Dynamic Local Discovery:** Uses `FusedLocationProviderClient` (`lastLocation` + `getCurrentLocation` fallback). When testing outside Jabalpur/Bhopal pilot coordinates (>25 km), `FakeStationRepository` generates nearby local stations so the map is always populated around the user's real GPS location.
- **Station & Charger Selection (`StationDetailsScreen`, `ChargerSelectionScreen`):** Favorite heart toggle, station navigation intent, amenities, operating hours, and real-time gun/connector availability (`CCS2`, `Type 2`, kW rating, `ChargerStatusChip`).
- **QR Scan & Charging (`QrScannerScreen`, `ChargingQuoteScreen`, `ChargingStartConfirmationScreen`, `ActiveChargingScreen`, `ChargingSummaryScreen`):**
  - Animated viewfinder laser scanner + manual Charger ID dialog + one-tap simulation (`EVCHARGE:CHG-01-DC`).
  - Itemized tariff quote card (energy rate, activation fee, parking fee, idle fee, estimated total) with **Spacetime Wallet** or **Direct UPI** payment selection.
  - Pre-start safety checklist (`Connector locked`, `Ignition off`, `Payment authorized`).
  - Animated active charging screen polling meter telemetry (`kWh`, `kW`, duration, estimated ₹) with a bottom-sheet Stop confirmation and itemized GST receipt summary.
- **My Charging Sessions (`MyChargingSessionsScreen`):** Total completed sessions, total energy added (`kWh`), live session quick jump, past receipt cards, and a **Scan & Charge** FAB.
- **Wallet (`WalletDashboardScreen`, `AddMoneyScreen`, `WalletTransactionsScreen`):** Spendable & promotional balances, preset top-ups (`₹100`, `₹200`, `₹500`, `₹1000`), gateway modes (UPI Intent, Card, NetBanking), and category-filtered ledger (`All`, `Top-ups`, `Charging`, `Cashback / Refunds`).
- **Profile & EV Garage (`CustomerProfileScreen`, `VehicleListScreen`, `AddVehicleScreen`, `SavedStationsScreen`, `NotificationsScreen`, `HelpSupportScreen`):** EV impact stats (sessions, kWh, CO2 offset), multi-vehicle garage with default toggle & connector specs (`CCS2`, `Type 2`, `GB/T`, `CHAdeMO`), saved hubs, categorized alerts, and SLA support ticket submission.

### 3. Station Owner / Site Admin Portal (`SiteAdminShell`)
Bottom Navigation Tabs: **Overview** • **Stations** • **Sessions** • **Revenue** • **Staff**

- **Overview:** Gross revenue, dispensed energy (`kWh`), sessions today, unique drivers, and live gun health breakdown (`Available`, `In Use`, `Offline / Faulted`).
- **Stations:** Multi-site readiness list showing open status, gun counts, tariffs (`₹/kWh`), and schedules.
- **Live Sessions:** Real-time active charging dispatches showing charger/gun, masked customer mobile (`+91 98765****10`), live `kW`/`kWh`, and accrued ₹.
- **Revenue & Settlements:** Net settlement balance (paid vs. pending clearing), accounting breakdown (**Gross Collection**, **Platform Commission 10%**, **Gateway Fee 2%**, **GST 18%**), and weekly settlement statement history.
- **Staff:** Role-based personnel directory (`Site Manager`, `Station Operator`, `Accountant`) with an invite dialog.

### 4. Service Provider Portal (`ProviderShell`)
Bottom Navigation Tabs: **Overview** • **Installs** • **Service** • **Assets** • **Staff**

- **Overview:** Enterprise client count, chargers installed, active/pending installations, open tickets, and expiring warranties.
- **Installations:** Turnkey lifecycle tracking (`SURVEY_PENDING` → `QUOTATION_SUBMITTED` → `INSTALLATION_IN_PROGRESS` → `COMMISSIONING_PENDING` → `COMPLETED`) with ability to create new projects and advance stages.
- **Service & SLA Tickets:** Priority queue (`HIGH` / `MEDIUM` / `LOW`), SLA countdowns, technician assignment, and ticket resolution.
- **Equipment Inventory:** Serialized EVSE tracking (`INSTALLED`, `DISPATCHED`, `IN_STOCK`, `UNDER_MAINTENANCE`), rated kW, connectors, and warranty expiry dates.
- **Field Technicians:** Certified electrical & EVSE engineer directory showing service region, skills, and `AVAILABLE` / `ON_DUTY` status.

---

## 🔄 Detailed Feature Flows & Diagrams

### 1. Authentication & Multi-Role Routing

```mermaid
flowchart TD
    Launch(["App Launch"]) --> Splash["SplashScreen (checks TokenManager)"]
    Splash -->|No token or error| Welcome["WelcomeScreen (+91 10-digit input)"]
    Splash -->|Valid session| CheckUser["AuthRepository.getCurrentUser()"]

    Welcome -->|Send OTP| OTP["OtpVerificationScreen (6-digit auto-verify, 30s resend)"]
    OTP -->|isNewUser = true| Onboard["OnboardingScreen (Name, Email, City, Terms)"]
    Onboard --> CustShell

    OTP -->|Existing user| RolesCheck{"Number of Roles"}
    CheckUser --> RolesCheck

    RolesCheck -->|Multiple roles| RoleSelect["RoleSelectionScreen"]
    RolesCheck -->|Single CUSTOMER| CustShell["CustomerShell"]
    RolesCheck -->|Single SITE_ADMIN| AdminShell["SiteAdminShell"]
    RolesCheck -->|Single SERVICE_PROVIDER| ProvShell["ProviderShell"]

    RoleSelect -->|Customer| CustShell
    RoleSelect -->|Site Admin| AdminShell
    RoleSelect -->|Service Provider| ProvShell
```

### 2. Station Discovery & Multi-Engine Map Engine

```mermaid
flowchart TD
    OpenExplore["Open Explore Tab"] --> CheckPerm{"Location Permission?"}
    CheckPerm -->|Not granted| AskPerm["Request FINE / COARSE Permission"]
    AskPerm -->|Granted| GetLoc
    AskPerm -->|Denied| DefaultCoords["Use Default Pilot Coords (Jabalpur)"]

    CheckPerm -->|Granted| GetLoc["FusedLocationProvider.lastLocation"]
    GetLoc -->|Null on emulator/fresh boot| FreshLoc["getCurrentLocation(HIGH_ACCURACY)"]
    GetLoc -->|Available| FetchStations
    FreshLoc -->|Coords found| FetchStations
    FreshLoc -->|Null / failed| DefaultCoords --> FetchStations

    FetchStations["StationRepository.getNearbyStations(lat, lng)"] --> CheckDist{"Nearest pilot <= 25 km?"}
    CheckDist -->|Yes| SortFilter["Haversine Distance + Quick Filter / Search / Sort"]
    CheckDist -->|No| GenLocal["Generate 5 Local Stations Within 0.8-2.8 km"] --> SortFilter

    SortFilter --> ViewMode{"View Mode"}
    ViewMode -->|Map Mode| Engine{"MapEngine"}
    ViewMode -->|List Mode| ListUI["StationListScreenContent"]

    Engine -->|Default| OSM1["OsmRealMapView (OpenStreetMap MAPNIK)"]
    Engine -->|Clean Map| OSM2["OsmRealMapView (Carto Voyager)"]
    Engine -->|Google Maps| GMap["GoogleMapsView (checks MAPS_API_KEY, offers OSM fallback)"]
    Engine -->|Offline| VecMap["NativeMapView (Compose Canvas Vector)"]
```

### 3. End-to-End Charging & Billing Sequence

```mermaid
sequenceDiagram
    actor Driver
    participant UI as Discovery / QR Screen
    participant StationScreen as StationDetails and ChargerSelection
    participant Quote as ChargingQuoteScreen
    participant Active as ActiveChargingScreen
    participant Repo as ChargingRepository

    alt Discover via Map
        Driver->>UI: Tap station pin then Charge Here
        UI->>StationScreen: Navigate to stationId
        Driver->>StationScreen: Select Charger and Gun 1
        StationScreen->>Repo: createQuote(stationId, chargerId, connectorId)
    else Scan Charger QR
        Driver->>UI: Scan QR or Enter Charger ID
        UI->>Repo: resolveQr(qrPayload)
    end

    Repo-->>Quote: ChargingQuote (rate per kWh, est. total, wallet balance)
    Driver->>Quote: Select WALLET or UPI then Continue
    Driver->>Active: Confirm safety checklist then Start Charging
    Active->>Repo: startSession(quoteId, paymentMode)
    Repo-->>Active: sessionId

    loop Every 3 seconds while CHARGING
        Active->>Repo: getActiveSession()
        Repo-->>Active: powerKw, energyKwh, estimatedAmount
    end

    Driver->>Active: Stop Charging Session then Confirm and Stop
    Active->>Repo: stopSession(sessionId)
    Repo-->>Active: SessionSummary (total kWh, GST 18%, finalAmount)
    Active->>Driver: Display ChargingSummaryScreen Receipt
```

### 4. Charging Session State Lifecycle

```mermaid
stateDiagram-v2
    [*] --> CREATED : Quote resolved or gun selected
    CREATED --> STARTING : startCharging triggered
    STARTING --> CHARGING : Charger accepted start command
    STARTING --> FAILED : Eligibility or handshake error
    CHARGING --> STOPPING : Driver confirms Stop sheet
    STOPPING --> COMPLETED : Final meter and tax reconciled
    CHARGING --> COMPLETED : Remote stop or history logged
    COMPLETED --> [*]
    FAILED --> [*]
```

### 5. Wallet Top-Up & Site Revenue Flow

```mermaid
flowchart LR
    subgraph CustomerWallet["Customer Wallet & Ledger"]
        WCard["WalletDashboardScreen"] --> Add["AddMoneyScreen (Presets ₹100-₹1000)"]
        Add --> PayMode["Gateway Mode (UPI Intent / Card / NetBanking)"]
        PayMode --> Credit["WalletRepository.createTopUp()"]
        Credit --> Ledger["WalletTransactionsScreen (Filter: All / Top-up / Charge / Refund)"]
    end

    subgraph SiteSettlement["Site Admin Settlement Accounting"]
        Gross["Gross Charging Collection"] --> Ded1["- Platform Commission (10%)"]
        Ded1 --> Ded2["- Payment Gateway Fee (2%)"]
        Ded2 --> Ded3["- GST on Convenience (18%)"]
        Ded3 --> Net["Net Settlement Payable -> Settlement Statements"]
    end
```

---

## 🎨 Tech Stack & Design System

### Core Libraries (from `gradle/libs.versions.toml`)
- **Language & Build:** Kotlin `2.1.0`, Android Gradle Plugin `8.7.3`, KSP `2.1.0-1.0.29`, `minSdk 26`, `compileSdk 35`, `targetSdk 35`
- **UI & Navigation:** Jetpack Compose BOM `2025.01.01`, Material 3, Material Icons Extended, Compose Navigation `2.8.5` (type-safe routes via `kotlinx-serialization-json 1.7.3`)
- **DI & Async:** Hilt `2.52`, Kotlin Coroutines `1.9.0`
- **Maps & Location:** `osmdroid-android 6.1.20` (default street & Carto Voyager maps), Google Maps Compose `6.2.1`, `play-services-location 21.3.0`
- **Networking & Storage:** Retrofit `2.11.0`, OkHttp `4.12.0`, `androidx.security:security-crypto 1.1.0-alpha06` (`EncryptedSharedPreferences`), DataStore Preferences `1.1.2`
- **Testing:** JUnit `4.13.2`, MockK `1.13.13`, Turbine `1.2.0`, `kotlinx-coroutines-test`

### Brand Palette (`core:designsystem`)
| Token | Hex | Usage |
|---|---|---|
| `Primary` | `#087F5B` | Primary CTAs, active tabs, EV availability highlights |
| `PrimaryLight` | `#0CA678` | Brand gradients, dark-mode/navy contrast accents |
| `Secondary` | `#102A43` | Wallet/revenue dark hero cards, high-emphasis elements |
| `AppBackground` | `#F6F8FA` | Screen surface canvas |
| `CardBackground` | `#FFFFFF` | Elevated cards, sheets, top/bottom bars |
| `Success` / `Warning` / `Error` | `#16A34A` / `#F59E0B` / `#DC2626` | Connector health, occupied states, faults & SLA alerts |

---

## 🛠️ Running Locally & Testing

### Launch in Android Studio
1. Open `/Users/live4yashi/Documents/Spacetime` in **Android Studio Ladybug (2024.2+)** or newer.
2. Ensure **Gradle JDK 17+** is selected (`Settings → Build, Execution, Deployment → Build Tools → Gradle`).
3. Let Gradle sync download dependencies (including `osmdroid-android` for OpenStreetMap).
4. Select the **`app`** run configuration and run on an emulator or physical Android device (`API 26+`).
5. **Demo Sign-In:** Enter any 10-digit mobile number and any 6-digit OTP to test all three workspaces (`Customer`, `Site Admin`, `Service Provider`).
6. *(Optional Google Maps)*: OpenStreetMap works out of the box with no key; to also enable the Google Maps layer option, add `MAPS_API_KEY=your_key` to `local.properties` or `gradle.properties`.

### Unit Tests
Unit test suites exist across feature ViewModels (`WelcomeViewModelTest`, `StationDiscoveryViewModelTest`, `ChargingViewModelTest`, `WalletViewModelTest`, `VehiclesViewModelTest`, `SiteRevenueViewModelTest`, `ProviderInstallationsViewModelTest`) and can be run directly from Android Studio's test runner or Gradle (`testDebugUnitTest`).

### Companion Documentation
- [`IMPLEMENTATION_PLAN.md`](IMPLEMENTATION_PLAN.md) — Module progress & cloud production roadmap (Phases 9–12)
- [`PRODUCTION_SETUP_GUIDE.md`](PRODUCTION_SETUP_GUIDE.md) — Cloud backend, PostgreSQL schema, TRAI DLT + MSG91/Twilio OTP, Razorpay webhooks, and API cutover steps
- [`api/openapi.json`](api/openapi.json) — Authoritative OpenAPI 3.1 REST & WebSocket contract
