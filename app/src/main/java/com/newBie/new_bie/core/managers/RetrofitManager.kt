package com.newBie.new_bie.core.managers

import com.newBie.new_bie.App
import com.newBie.new_bie.core.utils.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    val context = App.instance
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API.SUPABASE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}