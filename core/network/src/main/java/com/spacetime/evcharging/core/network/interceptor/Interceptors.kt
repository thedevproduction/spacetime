package com.spacetime.evcharging.core.network.interceptor

import com.spacetime.evcharging.core.security.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = runBlocking { tokenManager.getAccessToken() }
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}

@Singleton
class RequestIdInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Request-Id", UUID.randomUUID().toString())
            .addHeader("Accept-Language", "en-IN")
            .addHeader("Accept", "application/json")
            .build()
        return chain.proceed(request)
    }
}
