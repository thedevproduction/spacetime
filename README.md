# Spacetime Energy Futures — EV Charging Platform & Android Architecture

A production-grade, multi-role EV mobility platform built for the Indian market using **Kotlin**, **Jetpack Compose**, **Material 3**, **Clean Architecture**, and an **authoritative REST & WebSocket backend contract**.

---

## ⚡ Milestone Implementation Status

| Milestone | Scope & Deliverables | Status |
|---|---|:---:|
| **Milestone 1 — UX Foundation** | Modular architecture, Material 3 design tokens, Reusable component suite, Navigation graph, Authentication flow (Splash, Welcome, OTP, Onboarding, Role Selection), Role-specific shell navigation, Mock data fakes, Unit tests | ✅ Completed |
| **Milestone 2 — Customer Discovery** | Registered vehicle garage (`CUS-005`), Interactive Explore Map with color-coded status pins (`CUS-007`), Station List with 4 sorting modes (`CUS-008`), Filter bottom sheet (`CUS-009`), Station Details (`CUS-010`), Charger & connector selection (`CUS-011`) | ✅ Completed |
| **Milestone 3 — Real Charging Journey** | Camera QR scanner with manual ID fallback (`CUS-012`), Transparent tariff snapshot (`CUS-013`), Payment selection (`CUS-014`), Safety start checklist (`CUS-015`), Real-time active charging telemetry (`CUS-016`), Stop confirmation (`CUS-017`), Authoritative billing receipt & PDF invoice (`CUS-018`) | ✅ Completed |
| **Milestone 4 — Wallet & Payments** | Immutable wallet ledger (`CUS-020`), Top-up with preset chips & UPI Intent (`CUS-021`), Category-filtered transactions (`CUS-022`), Refund and promotional cashback isolation | ✅ Completed |
| **Milestone 5 — Site Admin Operations** | Multi-station telemetry (`ADM-001`), Station network overview (`ADM-002`, `ADM-003`), Live sessions monitor (`ADM-006`), Net revenue deductions accounting (`ADM-009`), Settlement statements history (`ADM-010`), Staff access control (`ADM-013`) | ✅ Completed |
| **Milestone 6 — Service Provider Operations** | Installation turnkey pipeline (`SP-004`, `SP-005`), Serialized equipment asset tracker (`SP-009`), SLA maintenance tickets & technician assignment (`SP-011`), Field engineers directory (`SP-013`) | ✅ Completed |
| **Milestone 7 — Super Admin & API Contract** | Authoritative OpenAPI 3.1 specification ([`api/openapi.json`](file:///Users/live4yashi/Documents/Spacetime/api/openapi.json)) covering all REST endpoints, WebSocket schemas, and role scopes | ✅ Completed |
| **Milestone 8 — Production Readiness** | Clean dependency isolation, unit test coverage across ViewModels, Hilt DI modules, ProGuard optimization rules | ✅ Completed |

---

## 🎨 Design System Tokens

```
Primary:        #087F5B (Main actions, brand identity, available connectors)
Primary Dark:   #056044 (Pressed states, heavy visual accents)
Secondary:      #102A43 (Headings, dark surfaces, cards)
App Background: #F6F8FA (Soft neutral background)
Card Surface:   #FFFFFF (Elevated cards, bottom sheets)
Success:        #16A34A (Available equipment, completed charging)
Warning:        #F59E0B (Occupied connectors, pending dispatches)
Error:          #DC2626 (Faulted chargers, payment failures)
Border:         #E5E7EB (Dividers and hairline strokes)
```

---

## 📱 Navigation Shells & Role Routing

```text
                        APP LAUNCH
                            │
                            ▼
                     [ SplashScreen ]
                            │
              ┌─────────────┴─────────────┐
        Not Logged In                  Logged In
              │                           │
              ▼                           ▼
      [ WelcomeScreen ]            Fetch User Roles
              │                           │
              ▼                  ┌────────┴────────┐
      [ OtpVerifyScreen ]    Single Role      Multiple Roles
              │                  │                 │
              ▼                  ▼                 ▼
     [ OnboardingScreen ]  Open Dashboard   [ RoleSelection ]
```

1. **EV Customer (`CustomerShell`)**:
   - `Home` ➔ Wallet balance, quick scan, nearby hubs, active session banner.
   - `Explore` ➔ Interactive map, list toggle, real-time status pins, filter bottom sheet.
   - `My Charging` ➔ Live telemetry (kW, kWh, ₹, duration) and session history.
   - `Wallet` ➔ Balance card, quick top-ups (₹100, ₹200, ₹500, ₹1000), transaction ledger.
   - `Garage` ➔ Registered electric vehicles, default vehicle toggle, connector specs.

2. **Station Owner (`SiteAdminShell`)**:
   - `Dashboard` ➔ Multi-site telemetry, gross collections, dispensed kWh, gun availability.
   - `Stations` ➔ Station operational readiness, tariffs, operating hours.
   - `Sessions` ➔ Real-time power draws, energy, and masked customer identifiers.
   - `Revenue` ➔ Net settlement calculations, gateway fees (2%), platform fee (10%), GST (18%).
   - `Staff` ➔ Role-based access invitations (Site Manager, Operator, Accountant).

3. **Service Provider (`ProviderShell`)**:
   - `Dashboard` ➔ Active installations, equipment installed, expiring warranties, open tickets.
   - `Installations` ➔ Turnkey lifecycle (Survey, Quotation, Installation, Commissioning, Handover).
   - `Service` ➔ Repair SLA tickets with countdown timers and field technician dispatch.
   - `Equipment` ➔ Serialized asset tracking with warranty expiry dates.
   - `Team` ➔ Certified electrical and civil field technicians directory.

---

## 🧪 Unit Testing Suite

Unit test suites covering state transitions, repository delegation, and reactive flows:
- [`WelcomeViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-auth/src/test/java/com/spacetime/evcharging/feature/auth/WelcomeViewModelTest.kt) (Phone validation & OTP dispatch)
- [`VehiclesViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-vehicles/src/test/java/com/spacetime/evcharging/feature/vehicles/VehiclesViewModelTest.kt) (Vehicle garage CRUD)
- [`StationDiscoveryViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-station-discovery/src/test/java/com/spacetime/evcharging/feature/station/discovery/StationDiscoveryViewModelTest.kt) (Map/List toggle & text search filtering)
- [`ChargingViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-charging/src/test/java/com/spacetime/evcharging/feature/charging/ChargingViewModelTest.kt) (Quote resolution & start state machine)
- [`WalletViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-wallet/src/test/java/com/spacetime/evcharging/feature/wallet/WalletViewModelTest.kt) (Balance updates & transaction filtering)
- [`SiteRevenueViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-site-revenue/src/test/java/com/spacetime/evcharging/feature/site/revenue/SiteRevenueViewModelTest.kt) (Deduction calculations & statement periods)
- [`ProviderInstallationsViewModelTest.kt`](file:///Users/live4yashi/Documents/Spacetime/feature-provider-installations/src/test/java/com/spacetime/evcharging/feature/provider/installations/ProviderInstallationsViewModelTest.kt) (Installation pipeline & dialog toggles)

---

## 🛠️ Build & Run Instructions

1. Open project in **Android Studio Ladybug (2024.2+)** or later.
2. Select **Gradle JDK 17+**.
3. Sync Gradle dependencies.
4. Target an Android device or emulator running **API 26 (Android 8.0)** or higher.
