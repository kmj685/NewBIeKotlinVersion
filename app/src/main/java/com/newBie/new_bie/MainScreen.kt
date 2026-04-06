package com.newBie.new_bie

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.newBie.new_bie.core.NavHost.AppNavHost
import com.newBie.new_bie.core.managers.SupabaseManager.supabase
import com.newBie.new_bie.core.utils.Routes
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        supabase.auth.sessionStatus.collect {
            when(it) {
                is SessionStatus.Authenticated -> {


                    it.session.user?.id?.let {
                        navController.navigate(Routes.HOME) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
//                        SupabaseManager.fetchUser(it)
//                        val userNickname = SupabaseManager.userInfoState.value?.nickName
//                        if(userNickname == null) navController.navigate("setNickName")
//                        val questSet = SupabaseManager.fetchQuestSet(it)
//                        if (questSet == null) {
//                            navController.navigate("setNickName"){
//                                // 기존 화면 스택들 모두 날리기
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                                // 화면 하나만 나오게 처리
//                                launchSingleTop = true
//                            }
//                        } else if(users?.unregisterAt != null) {
//                            Toast.makeText(context, "탈퇴한 회원입니다.",
//                                Toast.LENGTH_SHORT).show()
//                            navController.navigate("unregisterUser"){
//                                // 기존 화면 스택들 모두 날리기
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                                // 화면 하나만 나오게 처리
//                                launchSingleTop = true
//                            }
//                        } else {
//                            SupabaseManager.fetchQuestRecord(it)
//                            Toast.makeText(context, "어서오세요 ${users?.nickName?: "user"}님",
//                                Toast.LENGTH_SHORT).show()
//                            navController.navigate("main"){
//                                // 기존 화면 스택들 모두 날리기
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                                // 화면 하나만 나오게 처리
//                                launchSingleTop = true
//                            }
//                        }

                    }
//                    it.session.user?.id?.let { blockedUserId ->
//                        val fetchedUser = SupabaseManager.fetchUser(blockedUserId)
//                        if (fetchedUser.isBanned) {
//                            navController.navigate("blocked_user/${blockedUserId}?name=${fetchedUser.nickname}")  {
//                                // 기존 화면 스택들 모두 날리기
//                                popUpTo(navController.graph.startDestinationId) {
//                                    inclusive = true
//                                }
//                                // 화면 하나만 나오게 처리
//                                launchSingleTop = true
//                            }
//                        } else {
//                            isAuthLoggedIn = true
//                        }
//                    }


                    println("Received new authenticated session.")

//                    when(it.source) { //Check the source of the session
//                        SessionSource.External -> TODO()
//                        is SessionSource.Refresh -> TODO()
//                        is SessionSource.SignIn -> TODO()
//                        is SessionSource.SignUp -> TODO()
//                        SessionSource.Storage -> TODO()
//                        SessionSource.Unknown -> TODO()
//                        is SessionSource.UserChanged -> TODO()
//                        is SessionSource.UserIdentitiesChanged -> TODO()
//                    }
                }
                SessionStatus.Initializing -> println("Initializing")
                is SessionStatus.RefreshFailure -> {
                    println("Session expired and could not be refreshed")
                }
                is SessionStatus.NotAuthenticated -> {
//                    navController.navigate("login"){
//                        // 기존 화면 스택들 모두 날리기
//                        popUpTo(navController.graph.startDestinationId) {
//                            inclusive = true
//                        }
//                        // 화면 하나만 나오게 처리
//                        launchSingleTop = true
//                    }
                    if(it.isSignOut) {
                        println("User signed out")
                    } else {
                        println("User not signed in")
                    }
                }
            }
        }
    }

    AppNavHost(modifier = modifier.fillMaxSize(), navController = navController, context = context)

//    NavHost(modifier = modifier.fillMaxSize(), navController = navController,
//        startDestination = "/splash",
//        builder = {
//            composable(route = "/splash") {
//
//            }
//            composable(route = "/home") {
//
//            }
//            composable(route = "/search") {
//
//            }
//            composable(route = "/add") {
//
//            }
//            composable(route = "/journal") {
//
//            }
//            composable(route = "/myProfile") {
//
//            }
//            composable(route = "/setting") {
//
//            }
//            composable(route = "/question") {
//
//            }
//            composable(route = "/notice") {
//
//            }
//            composable(route = "/:notice_id") {
//
//            }
//            composable(route = "/blocked_users") {
//
//            }
//            composable(route = "/follower") {
//
//            }
//            composable(route = "/:notice_id") {
//
//            }
//
//
//        }
//    )
}