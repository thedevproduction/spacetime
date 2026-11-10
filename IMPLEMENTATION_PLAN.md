# Spacetime Energy Futures — Master Implementation Plan

This document tracks the full end-to-end implementation status of the **Spacetime EV Charging Platform**—covering the completed Android client foundation (Milestones 1–8 + Map & UI/UX upgrades) and the upcoming **Cloud Production Integration phases** (companion to [`PRODUCTION_SETUP_GUIDE.md`](PRODUCTION_SETUP_GUIDE.md)).

---

## 1. Platform Overview

- **Client Stack:** Kotlin, Jetpack Compose, Material 3, Hilt DI, Type-Safe Navigation (`kotlinx.serialization`), Retrofit + OkHttp, `EncryptedSharedPreferences`, `osmdroid` (OpenStreetMap).
- **Roles Supported:** EV Customer (`CustomerShell`), Station Owner / Site Admin (`SiteAdminShell`), Service Provider (`ProviderShell`), Platform Super Admin.
- **API Contract:** Authoritative OpenAPI 3.1 specification in [`api/openapi.json`](api/openapi.json).

---

## 2. Module Architecture

```text
app/                            → Main entry, Hilt App, SpacetimeNavHost, Role Shells
api/                            → OpenAPI 3.1 backend specification (openapi.json)
core/
  ├── common/                   → Constants, Money, AppResult, UiState, Enums
  ├── datastore/                → DataStore user preferences & active role cache
  ├── designsystem/             → Theme, Colors, Typography, Shapes, Reusable Components
  ├── navigation/               → Type-safe routes & NavigationEvent definitions
  ├── network/                  → Retrofit ApiServices, DTOs, Auth & RequestId Interceptors
  ├── security/                 → Encrypted TokenManager (JWT access + refresh)
  └── testing/                  → Coroutine MainDispatcherRule & test utilities
data/
  ├── di/                       → DataModule (currently binds Fake* implementations)
  ├── model/                    → Domain models (Station, Charger, Wallet, Session, etc.)
  └── repository/               → Repository interfaces + fake/ (to add remote/)
feature-auth/                   → Splash, Welcome, OTP Verify, Onboarding, Role Selection
feature-customer-home/          → Customer dashboard, quick actions, active session banner
feature-station-discovery/      → OpenStreetMap (osmdroid) + filters + search + list toggle
feature-station-details/        → Station overview, real-time gun status, charger selection
feature-qr-scanner/             → QR viewfinder with scan laser & manual charger ID entry
feature-charging/               → Tariff quote, start confirmation, live telemetry, history, receipt
feature-wallet/                 → Wallet balance, UPI/Card top-up, ledger transactions
feature-vehicles/               → EV garage CRUD & default vehicle selection
feature-customer-profile/       → Profile, EV CO2 impact stats, saved stations, notifications, help
feature-site-dashboard/         → Site Admin KPIs & charger network health
feature-site-stations/          → Site station & tariff configuration
feature-site-sessions/          → Live site charging sessions monitor
feature-site-revenue/           → Revenue breakdown, deductions & settlement statements
feature-site-employees/         → Role-based staff invitations & permissions
feature-provider-dashboard/     → Service Provider operational KPIs
feature-provider-installations/ → Turnkey installation pipeline (Survey → Handover)
feature-provider-equipment/     → Serialized charger & asset inventory
feature-provider-service/       → SLA maintenance tickets & technician dispatch
feature-provider-technicians/   → Certified field engineer directory
feature-provider-warranty/      → Warranty & AMC expiration tracking
```

---

## 3. Completed Client Milestones (Mock / Demo Mode)

| Phase | Scope & Deliverables | Status |
|---|---|---|
| **Milestone 1 — UX Foundation** | Modular Gradle project (20+ modules), Material 3 design tokens, reusable component library, type-safe navigation, Splash/Welcome/OTP/Onboarding/Role Selection, 3 role shells, fake repositories | ✅ Completed |
| **Milestone 2 — Customer Discovery** | Registered EV garage (`CUS-005`), interactive map & list toggle (`CUS-007`, `CUS-008`), filter sheet (`CUS-009`), station details & charger gun selection (`CUS-010`, `CUS-011`) | ✅ Completed |
| **Milestone 3 — Charging Journey** | QR scanner + manual charger ID (`CUS-012`), tariff quote (`CUS-013`), payment mode selector (`CUS-014`), safety checklist (`CUS-015`), live session telemetry (`CUS-016`), stop flow (`CUS-017`), receipt summary (`CUS-018`) | ✅ Completed |
| **Milestone 4 — Wallet & Payments** | Spendable/promo wallet balance (`CUS-020`), preset top-up flow (`CUS-021`), filtered ledger (`CUS-022`) | ✅ Completed |
| **Milestone 5 — Site Admin Ops** | Multi-station KPIs (`ADM-001`), stations (`ADM-002`/`003`), live sessions (`ADM-006`), revenue & commission breakdown (`ADM-009`), settlements (`ADM-010`), staff invites (`ADM-013`) | ✅ Completed |
| **Milestone 6 — Service Provider Ops** | Installation pipeline (`SP-004`/`005`), equipment serial inventory (`SP-009`), SLA repair tickets (`SP-011`), technician directory (`SP-013`), warranty tracking | ✅ Completed |
| **Milestone 7 — API Contract** | Authoritative REST + WebSocket contract in `api/openapi.json` and Retrofit DTOs/services in `core/network` | ✅ Completed |
| **Milestone 8 — Map & UI/UX Polish** | Native OpenStreetMap (`osmdroid`) with dynamic local stations & GPS fallback, dark-icon edge-to-edge status bars, single-line bottom nav, gesture-bar-safe sticky actions, upgraded cards, animated QR scanner | ✅ Completed |

---

## 4. Production Integration Roadmap (Next Phases)

Follow [`PRODUCTION_SETUP_GUIDE.md`](PRODUCTION_SETUP_GUIDE.md) to execute these phases in order:

### Phase 9 — Cloud Backend & Persistent Database
- [ ] Provision managed **PostgreSQL + PostGIS** (Supabase / Neon / AWS RDS) and **Redis** (Upstash / ElastiCache).
- [ ] Run database migrations for users, multi-role RBAC (`scope_type`/`scope_id`), vehicles, stations (PostGIS point), chargers/connectors, tariffs, immutable wallet ledger, charging sessions, settlements, installations, equipment, and service tickets.
- [ ] Deploy REST API service (FastAPI / NestJS / Go) to **Cloud Run / Railway / AWS ECS** behind HTTPS.
- [ ] Seed initial stations, chargers, tariffs, and admin/provider accounts.

### Phase 10 — Real Mobile SMS OTP & Session Security
- [ ] Complete **TRAI DLT** entity + header + OTP template registration (or use **Twilio Verify** for staging).
- [ ] Configure **MSG91** / Twilio credentials on the backend (`POST /api/v1/auth/otp/send` and `/auth/otp/verify`).
- [ ] Enforce Redis rate limiting (30s resend cooldown, max 5 verification attempts) and hashed OTP storage.
- [ ] Issue short-lived JWT `access_token` + rotating `refresh_token`; add OkHttp `Authenticator` in `core/network` for silent `401` token refresh.

### Phase 11 — Real Payments (Razorpay / UPI) & Wallet Reconciliation
- [ ] Set up **Razorpay** (Test Mode → Live KYC) and configure `RAZORPAY_KEY_ID`, `RAZORPAY_KEY_SECRET`, and `RAZORPAY_WEBHOOK_SECRET`.
- [ ] Implement `POST /api/v1/wallet/topups` to create a Razorpay Order (`payment_order_id`) and store a `CREATED` order record.
- [ ] Integrate **Razorpay Android Checkout SDK** (`com.razorpay:checkout`) in `feature-wallet` (`AddMoneyScreen`) and `feature-charging`.
- [ ] Implement server-side `POST /api/v1/webhooks/razorpay` with HMAC-SHA256 signature verification and atomic `SERIALIZABLE` ledger credit on `payment.captured`.
- [ ] Implement wallet hold (`available_balance_minor` → `reserved_balance_minor`) on session start and final meter settlement on session stop.

### Phase 12 — Android Live API Cutover, OCPP & Launch
- [ ] Update [`Constants.kt`](core/common/src/main/java/com/spacetime/evcharging/core/common/Constants.kt) / `BuildConfig` flavors (`dev` vs `prod`) with real `https://` and `wss://` endpoints.
- [ ] Add missing endpoints to [`ApiServices.kt`](core/network/src/main/java/com/spacetime/evcharging/core/network/api/ApiServices.kt) (vehicles, charging history/summary, site admin settlements/staff, service provider CRUD).
- [ ] Create `Remote*Repository` implementations in `data/src/main/java/com/spacetime/evcharging/data/repository/remote/` and switch [`DataModule.kt`](data/src/main/java/com/spacetime/evcharging/data/di/DataModule.kt) from `Fake*` to `Remote*`.
- [ ] Connect WebSocket listener (`wss://.../ws/v1/customer`) for live `ActiveChargingScreen` telemetry and OCPP 1.6J/2.0.1 charger events.
- [ ] Verify end-to-end production tests: real OTP login, ₹1–₹10 UPI top-up, multi-device persistence, RBAC `403` enforcement, duplicate `Idempotency-Key` safety, and signed release APK (`isMinifyEnabled = true`).

