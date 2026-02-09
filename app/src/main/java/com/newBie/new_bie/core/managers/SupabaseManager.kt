package com.newBie.new_bie.core.managers

import com.newBie.new_bie.App
import com.newBie.new_bie.core.utils.SupabaseInitial
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {
    val context = App.instance

    val supabase = createSupabaseClient(
        supabaseUrl = SupabaseInitial.URL,
        supabaseKey = SupabaseInitial.ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }

}