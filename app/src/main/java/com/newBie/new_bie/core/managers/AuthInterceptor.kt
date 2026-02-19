package com.newBie.new_bie.core.managers

import com.newBie.new_bie.core.utils.SupabaseInitial
import io.github.jan.supabase.auth.auth
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val session = SupabaseManager
            .supabase
            .auth
            .currentSessionOrNull()

        val token = session?.accessToken ?: SupabaseInitial.ANON_KEY

        val requestBuilder = chain.request().newBuilder()

        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}