# Spacetime Energy Futures — Production Cloud Setup Guide

This guide explains how to take the **Spacetime EV Charging Android App** from its current local mock mode (`Fake*Repository`) to a **live, production-ready cloud deployment** with:
1. **Real Mobile SMS OTP** (`+91` India DLT-compliant or global OTP)
2. **Real Online Payments** (UPI / Cards / NetBanking via Razorpay / Cashfree)
3. **Cloud Database Persistence** for all **Customers, Site Admins, and Service Providers** served via REST & WebSocket APIs

---

## 1. Current App Readiness vs. What Needs to Be Deployed

Your Android codebase already includes the full API contract and networking stack:
- **OpenAPI 3.1 Contract:** [`api/openapi.json`](api/openapi.json)
- **Retrofit Interfaces:** [`ApiServices.kt`](core/network/src/main/java/com/spacetime/evcharging/core/network/api/ApiServices.kt)
- **DTOs & Serialization:** [`core/network/dto/`](core/network/src/main/java/com/spacetime/evcharging/core/network/dto)
- **Auth & Request Interceptors:** [`Interceptors.kt`](core/network/src/main/java/com/spacetime/evcharging/core/network/interceptor/Interceptors.kt)
- **Encrypted Token Storage:** [`TokenManager.kt`](core/security/src/main/java/com/spacetime/evcharging/core/security/TokenManager.kt) (`AES256_GCM` `EncryptedSharedPreferences`)
- **Current Mock Binding:** [`DataModule.kt`](data/src/main/java/com/spacetime/evcharging/data/di/DataModule.kt) currently binds `Fake*Repository` classes.

To go live, you will:
1. Deploy a **Backend API + PostgreSQL Database + Redis** to the cloud.
2. Connect an **SMS OTP Provider** (MSG91 / Twilio / Firebase Auth).
3. Connect a **Payment Gateway** (Razorpay / Cashfree) with server-side webhook verification.
4. Implement `Remote*Repository` classes and swap the Hilt bindings in `DataModule.kt`.

---

## 2. Recommended Cloud Architecture

```text
┌───────────────────────────┐
│   Android App (Compose)   │
│  • Customer / Admin / SP  │
│  • Razorpay Checkout SDK  │
│  • Encrypted JWT Tokens   │
└─────────────┬─────────────┘
              │ HTTPS (REST) + WSS (Live Telemetry)
              ▼
┌───────────────────────────────────────────────────────────┐
│         Cloud Backend API (FastAPI / NestJS / Go)         │
│   Hosted on: AWS ECS / Google Cloud Run / Railway / Render│
│                                                           │
│  • Auth & JWT Service ──────► MSG91 / Twilio (SMS OTP)    │
│  • Payment & Ledger   ──────► Razorpay Orders + Webhooks  │
│  • Geospatial Discovery                                   │
│  • OCPP 1.6J / 2.0.1 Gateway (Real EV Charger Hardware)   │
└───────┬───────────────────────────────────────┬───────────┘
        │                                       │
        ▼                                       ▼
┌───────────────────────────┐       ┌───────────────────────┐
│   PostgreSQL + PostGIS    │       │         Redis         │
│ (Supabase / Neon / RDS)   │       │ (Upstash / ElastiCache│
│ • Users, Roles, Vehicles  │       │ • OTP rate-limits     │
│ • Stations, Chargers      │       │ • Idempotency keys    │
│ • Wallets & Ledger (ACID) │       │ • Live kW/kWh cache   │
│ • Installations & Tickets │       └───────────────────────┘
└───────────────────────────┘
```

### Recommended Cloud Providers (Low Ops → Enterprise)
| Component | Fast Launch / MVP Stack | Enterprise / Scale Stack |
|---|---|---|
| **Backend API** | Railway / Render / Google Cloud Run | AWS ECS Fargate / Google Kubernetes Engine |
| **PostgreSQL DB** | **Supabase** or **Neon** (managed Postgres + PostGIS) | AWS RDS PostgreSQL (Multi-AZ) |
| **Redis Cache** | **Upstash Redis** (serverless) | AWS ElastiCache / Memorystore |
| **Mobile OTP (India)** | **MSG91** or **Twilio Verify** | MSG91 Enterprise + AWS SNS fallback |
| **Payments (India)** | **Razorpay** (UPI Intent, Cards, NetBanking) | Razorpay / Cashfree + RazorpayX (payouts) |
| **File Storage (Invoices/Photos)** | Supabase Storage / Cloudflare R2 | AWS S3 + CloudFront |

---

## 3. Cloud Database Schema (PostgreSQL + PostGIS)

All Customer, Site Admin, and Service Provider state must live in PostgreSQL (never only on the mobile device).

### Core Tables Needed
1. **Identity & Multi-Role RBAC**
   - `users` (`id`, `full_name`, `country_code`, `mobile_number UNIQUE`, `email`, `city`, `preferred_language`, `status`, `created_at`)
   - `user_roles` (`id`, `user_id FK`, `role` [`CUSTOMER`, `SITE_ADMIN`, `SERVICE_PROVIDER`, `SUPER_ADMIN`], `scope_type`, `scope_id`, `status`)
   - `otp_challenges` (`challenge_id PK`, `mobile_number`, `otp_hash`, `purpose`, `attempts`, `expires_at`, `verified_at`)
   - `refresh_tokens` (`id`, `user_id FK`, `device_id`, `token_hash`, `revoked_at`, `expires_at`)

2. **Customer Discovery & Vehicles**
   - `vehicles` (`id`, `user_id FK`, `nickname`, `manufacturer`, `model`, `reg_number`, `battery_capacity_kwh`, `connector_types JSONB`, `max_ac_kw`, `max_dc_kw`, `is_default`)
   - `stations` (`id`, `owner_site_id`, `name`, `address`, `location GEOGRAPHY(Point, 4326)`, `is_open`, `operating_hours`, `amenities JSONB`, `rating`)
   - `chargers` (`charger_id PK`, `station_id FK`, `ocpp_identity UNIQUE`, `display_name`, `rated_power_kw`, `charger_type`, `status`)
   - `connectors` (`id`, `charger_id FK`, `connector_number`, `connector_type`, `status`, `tariff_id FK`)
   - `tariffs` (`id`, `energy_rate_minor_per_kwh`, `session_fee_minor`, `parking_fee_minor`, `idle_fee_minor_per_min`, `gst_percent`)

3. **Wallets, Payments & Charging Sessions**
   - `wallets` (`wallet_id PK`, `user_id UNIQUE FK`, `currency`, `available_balance_minor BIGINT`, `reserved_balance_minor BIGINT`, `promotional_balance_minor BIGINT`)
   - `wallet_transactions` (**append-only ledger**: `transaction_id`, `wallet_id FK`, `type` [`TOPUP`, `HOLD`, `CHARGE`, `REFUND`, `CASHBACK`], `amount_minor BIGINT`, `status`, `related_session_id`, `gateway_order_id`, `gateway_payment_id UNIQUE`, `created_at`)
   - `payment_orders` (`topup_id PK`, `user_id FK`, `gateway` [`RAZORPAY`], `gateway_order_id UNIQUE`, `amount_minor`, `status` [`CREATED`, `PAID`, `FAILED`], `created_at`)
   - `charging_quotes` (`quote_id PK`, `user_id FK`, `station_id`, `charger_id`, `connector_id`, `pricing_snapshot JSONB`, `estimated_total_minor`, `payment_method`, `expires_at`)
   - `charging_sessions` (`session_id PK`, `user_id FK`, `quote_id FK`, `charger_id`, `connector_id`, `ocpp_transaction_id`, `status`, `started_at`, `ended_at`, `energy_wh`, `power_w`, `final_amount_minor`, `tax_minor`, `payment_method`, `payment_status`)
   - `idempotency_keys` (`key PK`, `user_id`, `endpoint`, `response_json`, `created_at`)

4. **Site Admin & Service Provider Operations**
   - `site_staff` (`id`, `site_id`, `user_id FK`, `staff_role`, `invited_by`, `status`)
   - `settlements` (`id`, `site_id`, `period_start`, `period_end`, `gross_minor`, `gateway_fee_minor`, `platform_fee_minor`, `gst_minor`, `net_payout_minor`, `status`)
   - `installation_projects` (`id`, `provider_id`, `site_name`, `client_name`, `stage`, `target_date`, `assigned_lead`)
   - `equipment_assets` (`serial_number PK`, `provider_id`, `site_id`, `model`, `warranty_expiry`, `amc_expiry`, `status`)
   - `service_tickets` (`id`, `provider_id`, `station_id`, `charger_id`, `issue`, `priority`, `sla_deadline`, `assigned_technician_id`, `status`, `resolution_notes`)
   - `technicians` (`id`, `provider_id`, `user_id FK`, `specialization`, `region`, `availability_status`)

> **Important:** Store all money amounts as integer minor units (**paise**, `BIGINT`, e.g., `₹14.00` = `1400`) to avoid floating-point rounding bugs. This already matches `Money(amountMinor: Long)` and `*Minor` fields in your DTOs.

---

## 4. Setting Up Real Mobile OTP (Requirement #1)

Your app already calls:
- `POST /api/v1/auth/otp/send` (`SendOtpRequest` → `SendOtpResponse`)
- `POST /api/v1/auth/otp/verify` (`VerifyOtpRequest` → `VerifyOtpResponse`)

### Option A: MSG91 (Recommended for Production in India)
1. **Register on TRAI DLT Portal** (Jio TrueConnect / Airtel DLT / Vilpower) — mandatory by Indian telecom regulations before sending commercial SMS OTPs:
   - Register your **Entity ID** (company/proprietorship PAN & GST).
   - Register your **Sender ID** (Header, e.g. `SPCTME`).
   - Register an **OTP SMS Template** (e.g., `"Your Spacetime Energy verification code is ##OTP##. Valid for 5 minutes."`).
2. **Create an MSG91 Account:**
   - Enable the **OTP API** / Flow API and link your DLT Entity ID + Template ID.
   - Copy your `MSG91_AUTH_KEY` and `MSG91_TEMPLATE_ID` into backend environment variables.
3. **Backend Implementation (`/auth/otp/send`):**
   - Rate-limit by phone + IP in Redis (e.g., max 1 OTP per 30s matching `Constants.OTP_COUNTDOWN_SECONDS`, max 5 per hour).
   - Generate a 6-digit OTP via `crypto.randomInt(100000, 1000000)`.
   - Store `sha256(otp + salt)` in `otp_challenges` with `expires_at = NOW() + 5 minutes`.
   - Call MSG91 Send OTP API and return `{ challenge_id, masked_mobile_number, expires_in_seconds: 300, resend_after_seconds: 30 }`.
4. **Backend Implementation (`/auth/otp/verify`):**
   - Validate `challenge_id` exists, not expired, and `attempts < 5`.
   - Compare OTP hash in constant time; mark challenge verified.
   - Upsert `users` row (set `is_new_user = true` if `full_name` is null so the app routes to `OnboardingScreen`).
   - Issue **JWT `access_token`** (15m TTL) + **`refresh_token`** (30d TTL) matching `VerifyOtpResponse`.

### Option B: Twilio Verify (Fastest for Testing Before DLT Approval)
- Use **Twilio Verify Service** to avoid building your own OTP generator/DLT template during early staging tests, then migrate to MSG91 + DLT for India production scale/cost (~₹0.15–₹0.20 per SMS).

---

## 5. Setting Up Real Payments (Requirement #2)

Your app supports two payment flows:
1. **Wallet Top-Up:** `AddMoneyScreen` → `POST /api/v1/wallet/topups` (`TopUpRequest` → `TopUpResponse`)
2. **Direct Charging Payment / Authorization:** `ChargingQuoteScreen` → `POST /api/v1/charging/sessions/start`

### Razorpay Integration (Recommended for India: UPI, GPay, PhonePe, Cards, NetBanking)
1. **Create a Razorpay Account** (`dashboard.razorpay.com`):
   - Start in **Test Mode** immediately (no KYC wait needed to test UPI/Cards in sandbox).
   - Complete business KYC to unlock **Live Mode**.
   - Generate `RAZORPAY_KEY_ID` and `RAZORPAY_KEY_SECRET`.
   - Configure a Webhook endpoint (`https://api.yourdomain.com/api/v1/webhooks/razorpay`) subscribed to `payment.captured` and `payment.failed`, and save `RAZORPAY_WEBHOOK_SECRET`.

2. **Backend Flow (`POST /wallet/topups`):**
   - Backend validates `amount_minor >= 10000` (e.g., min ₹100).
   - Backend calls Razorpay Orders API (`POST https://api.razorpay.com/v1/orders`) with `{ amount: amount_minor, currency: "INR", receipt: topup_id }`.
   - Save a `CREATED` row in `payment_orders` and return `TopUpResponse(topupId, paymentOrderId = razorpayOrder.id, amountMinor, status = "CREATED")`.

3. **Android Checkout Flow:**
   - Add Razorpay Android SDK to `feature-wallet/build.gradle.kts` & `feature-charging/build.gradle.kts`:
     ```kotlin
     implementation("com.razorpay:checkout:1.6.40")
     ```
   - When `createTopUp()` returns `paymentOrderId`, launch `Checkout().open(activity, options)` with `order_id`, `amount`, `currency = "INR"`, and user's mobile/email prefilled.
   - Add Razorpay ProGuard keep rules in `app/proguard-rules.pro`.

4. **Authoritative Webhook Settlement (Never Credit Wallet From Client Alone):**
   - On `payment.captured` webhook:
     1. Verify `X-Razorpay-Signature` HMAC-SHA256 using `RAZORPAY_WEBHOOK_SECRET`.
     2. Start a PostgreSQL `SERIALIZABLE` / `SELECT ... FOR UPDATE` transaction.
     3. Check `gateway_payment_id` doesn't already exist in `wallet_transactions` (idempotency).
     4. Update `payment_orders.status = 'PAID'`, insert credit row into `wallet_transactions`, and increment `wallets.available_balance_minor`.
   - Android polls `GET /wallet` (or receives WebSocket event) after Razorpay SDK `onPaymentSuccess` callback to refresh the balance immediately.

5. **Charging Session Hold & Reconciliation:**
   - When `payment_mode == "WALLET"` on `POST /charging/sessions/start`:
     - Move `estimated_total_minor` from `available_balance_minor` → `reserved_balance_minor` (`HOLD` entry).
     - Send OCPP `RemoteStartTransaction` to charger.
   - On `StopTransaction` / `POST /charging/sessions/{id}/stop`:
     - Compute exact `final_amount_minor` from meter Wh + tariff + GST.
     - Capture actual amount from `reserved_balance_minor` and release any unused remainder back to `available_balance_minor`.

---

## 6. Connecting the Android App to Cloud APIs (Requirement #3)

Follow these exact code changes in this repository to switch from fake data to your live backend:

### Step 1: Configure Environment URLs (`Constants.kt` / `BuildConfig`)
Update [`Constants.kt`](core/common/src/main/java/com/spacetime/evcharging/core/common/Constants.kt) (or expose via `buildConfigField` in `app/build.gradle.kts` for `debug` vs `release`):
```kotlin
const val BASE_URL_DEV  = "https://staging-api.yourdomain.com/api/v1/"
const val BASE_URL_PROD = "https://api.yourdomain.com/api/v1/"
const val WS_URL_DEV    = "wss://staging-api.yourdomain.com/ws/v1/customer"
```
Ensure `NetworkModule.provideRetrofit(...)` selects `BASE_URL_PROD` for release builds and disables `HttpLoggingInterceptor.Level.BODY` in release.

### Step 2: Add Token Refresh Authenticator (`core/network`)
Add an OkHttp `Authenticator` alongside `AuthInterceptor` in [`Interceptors.kt`](core/network/src/main/java/com/spacetime/evcharging/core/network/interceptor/Interceptors.kt) so when the backend returns `HTTP 401`, OkHttp automatically calls `POST auth/token/refresh`, saves the new tokens in `TokenManager`, and retries the failed request seamlessly.

### Step 3: Complete Remote Retrofit Endpoints (`ApiServices.kt`)
[`ApiServices.kt`](core/network/src/main/java/com/spacetime/evcharging/core/network/api/ApiServices.kt) already defines core Auth, Station, Charging, Wallet, SiteAdmin, and Provider endpoints. Add the remaining endpoints defined in [`api/openapi.json`](api/openapi.json) and `Repositories.kt`:
- **Vehicles:** `GET /vehicles`, `POST /vehicles`, `DELETE /vehicles/{id}`, `POST /vehicles/{id}/default`
- **Charging History & Summary:** `GET /charging/sessions/{id}/summary`, `GET /charging/sessions/history`
- **Site Admin:** `GET /site-admin/revenue`, `GET /site-admin/settlements`, `GET /site-admin/sessions/live`, `GET /site-admin/staff`, `POST /site-admin/staff/invite`
- **Service Provider:** `GET/POST /service-provider/installations`, `PATCH /service-provider/installations/{id}/status`, `GET/POST /service-provider/equipment`, `GET/POST /service-provider/tickets`, `GET /service-provider/technicians`

### Step 4: Implement `Remote*Repository` Classes & Swap `DataModule.kt`
Create `data/src/main/java/com/spacetime/evcharging/data/repository/remote/` implementing:
- `RemoteAuthRepository` (calls `AuthApiService`, saves JWT tokens into `TokenManager`, caches active role in `UserPreferences`)
- `RemoteStationRepository`
- `RemoteChargingRepository` (generates UUID `Idempotency-Key` on `startSession`)
- `RemoteWalletRepository`
- `RemoteSiteAdminRepository`
- `RemoteServiceProviderRepository`
- `RemoteVehicleRepository`

Then update [`DataModule.kt`](data/src/main/java/com/spacetime/evcharging/data/di/DataModule.kt) to `@Binds` the `Remote*` implementations instead of `Fake*` (or use `debug`/`release` source sets so `debug` can optionally keep fakes and `release` always uses `Remote*`).

### Step 5: Real-Time Charger Updates (WebSocket)
Add an OkHttp `WebSocketListener` in `core/network` connecting to `WS_URL_DEV` (with `Authorization: Bearer <token>`) to stream live `power_kw`, `energy_kwh`, and `estimated_amount_minor` updates into `ActiveChargingScreen` and `SiteSessionsScreen` instead of polling.

---

## 7. Cloud Deployment & Environment Variables Checklist

### Backend `.env` Required Secrets
```env
# Server
PORT=8080
NODE_ENV=production
JWT_ACCESS_SECRET=<64-byte-random-hex>
JWT_REFRESH_SECRET=<64-byte-random-hex>

# Database & Cache
DATABASE_URL=postgresql://user:pass@host:5432/spacetime_ev?sslmode=require
REDIS_URL=rediss://default:pass@host:6379

# Mobile SMS OTP (MSG91 / Twilio)
OTP_PROVIDER=MSG91
MSG91_AUTH_KEY=<your-msg91-key>
MSG91_TEMPLATE_ID=<dlt-approved-template-id>
MSG91_SENDER_ID=SPCTME

# Payments (Razorpay)
RAZORPAY_KEY_ID=rzp_live_xxxxxxxxxxxx
RAZORPAY_KEY_SECRET=<your-razorpay-secret>
RAZORPAY_WEBHOOK_SECRET=<your-webhook-secret>
```

### Production Pre-Launch Verification Checklist
- [ ] **HTTPS + TLS 1.3** enabled on API & WebSocket domain (Cloud Run / Cloudflare / ALB).
- [ ] **TRAI DLT** entity + OTP template approved and live SMS OTP tested on physical Indian SIM cards.
- [ ] **Razorpay Live KYC** approved; `payment.captured` webhook tested end-to-end with real ₹1–₹10 UPI test transaction.
- [ ] **Database Backups** enabled (daily automated snapshots + Point-in-Time Recovery on Supabase/RDS).
- [ ] **Server-side Role Authorization** verified (a `CUSTOMER` token must receive `403 Forbidden` on `/site-admin/*` and `/service-provider/*` endpoints).
- [ ] **Idempotency** tested on `POST /charging/sessions/start` and payment webhooks (duplicate requests do not double-charge or start two sessions).
- [ ] **Android Release Build** configured with upload keystore, `isMinifyEnabled = true`, `Level.NONE` HTTP logging, and `BASE_URL_PROD`.

