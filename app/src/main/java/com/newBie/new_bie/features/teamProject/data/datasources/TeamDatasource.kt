package com.newBie.new_bie.features.teamProject.data.datasources

import com.newBie.new_bie.core.managers.RetrofitManager
import com.newBie.new_bie.core.managers.SupabaseManager

class TeamDatasource {
    val _supabase = SupabaseManager.supabase
    private var _retrofit = RetrofitManager.retrofit
}