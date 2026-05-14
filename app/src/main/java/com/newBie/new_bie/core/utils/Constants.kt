package com.newBie.new_bie.core.utils

import com.newBie.new_bie.core.managers.SupabaseManager.supabase
import io.github.jan.supabase.auth.auth

object Constants {
    val TAG : String = "로그"
}

object SupabaseInitial {
    const val URL : String = "https://syfgficcejjgtvpmtkzx.supabase.co"
    const val ANON_KEY : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN5ZmdmaWNjZWpqZ3R2cG10a3p4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIwNTUwNjksImV4cCI6MjA3NzYzMTA2OX0.Ng9atODZnfRocZPtnIb74s6PLeIJ2HqqSaatj1HbRsc"
    const val GOOGLE_WEB_CLIENT_ID : String = "970264997757-ageb9icp3uccibddetg0q1q1coeiiklm.apps.googleusercontent.com"
}

object API {
    const val SUPABASE_BASE_URL : String ="https://syfgficcejjgtvpmtkzx.supabase.co/functions/v1/post-function/"

    const val AUTHORIZATION : String = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN5ZmdmaWNjZWpqZ3R2cG10a3p4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjIwNTUwNjksImV4cCI6MjA3NzYzMTA2OX0.Ng9atODZnfRocZPtnIb74s6PLeIJ2HqqSaatj1HbRsc"
    const val CONTENT_TYPE : String = "application/json"
}

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val DELETED_USER = "deleted_user"
    const val BLOCKED = "blocked"
    const val UNREGISTER = "unregister"
    const val SET_PROFILE = "set_profile"

    const val HOME = "home"
    const val SEARCH = "search"
    const val ADD = "add"
    const val JOURNAL = "journal"
    const val MY_PROFILE = "my_profile"

    const val POST = "post"
    const val POST_EDIT = "edit"

    const val USER_PROFILE = "user_profile"
    const val FOLLOW = "follow"

    const val SETTING = "setting"
    const val QUESTION = "question"
    const val NOTICE = "notice"
    const val NOTICE_DETAIL = "notice_detail"
    const val BLOCKED_USERS = "blocked_users"
    const val UPDATE_PROFILE = "updateProfile"
    const val TEAM_PROJECT = "teamProject"
    const val CHATTING = "chatting"

    const val NOTIFICATION = "notification"
}


enum class PageSet {
    HOME, PROFILE, ADD_POST
}

enum class PageType{
    HOME, POST_DETAIL, SEARCH
}

enum class OrderByType{
    NEW_FIRST, OLD_FIRST, LIKES_FIRST
}

