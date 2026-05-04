package com.newBie.new_bie.core.NavHost

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.auth.presentation.screens.LoginScreen
import com.newBie.new_bie.features.post.presentation.screens.HomeScreen
import com.newBie.new_bie.features.post.presentation.screens.PostAddScreen
import com.newBie.new_bie.features.post.presentation.screens.PostDetailScreen
import com.newBie.new_bie.features.post.presentation.screens.SearchScreen
import com.newBie.new_bie.features.profile.presentation.screens.FollowScreen
import com.newBie.new_bie.features.profile.presentation.screens.MyProfileScreen
import com.newBie.new_bie.features.profile.presentation.screens.UpdateProfileScreen
import com.newBie.new_bie.features.teamProject.presentation.screens.TeamProjectListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(modifier : Modifier, navController: NavHostController, context: Context) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.SPLASH) {
            /* SplashScreen() */
        }

        composable(Routes.LOGIN) {
            LoginScreen(navController=navController)
        }

        composable(Routes.DELETED_USER) {
            /* DeletedUserScreen() */
        }

        composable(Routes.BLOCKED) {
            /* BlockedUserByAdminScreen() */
        }

        composable(Routes.UNREGISTER) {
            /* UnregisterScreen() */
        }

        composable(Routes.SET_PROFILE) {
            /* SetProfileScreen() */
        }

        // -------- Post --------
        composable(
            route = "${Routes.POST}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            PostDetailScreen(navController = navController, id = id)
        }

        composable(
            route = "${Routes.POST}/{id}/${Routes.POST_EDIT}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            /* PostEditScreen(postId) */
        }


        // -------- ShellRoute 대응 (BottomNav 영역) --------
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable("${Routes.HOME}/${Routes.SEARCH}?query={query}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
            ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchScreen(navController = navController, initialQuery = query)
        }

        composable(Routes.ADD) {
            PostAddScreen(navController = navController, context = context)
        }

        composable(Routes.JOURNAL) {
            /* JournalScreen() */
        }

        composable(Routes.MY_PROFILE) {
            MyProfileScreen(navController = navController)
        }

        composable(
            route = "${Routes.MY_PROFILE}/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")

            MyProfileScreen(navController = navController, targetUserId = userId)
        }

        composable(
            route = "${Routes.MY_PROFILE}/{userId}/${Routes.FOLLOW}?initialTab={initialTab}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("initialTab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            println("NavHost userId: $userId")
            val initialTab = backStackEntry.arguments?.getInt("initialTab")
            println("NavHose initialTab: $initialTab")

            FollowScreen(navController = navController, targetUserId = userId, initialTab = initialTab)
        }

        composable("${Routes.MY_PROFILE}/${Routes.SETTING}") {
            /* SettingScreen() */
        }

        composable("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.QUESTION}") {
            /* QuestionScreen() */
        }

        composable("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.NOTICE}") {
            /* NoticesScreen() */
        }

        composable(
            route = "${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.NOTICE}/{noticeId}",
            arguments = listOf(
                navArgument("noticeId") { type = NavType.IntType }
            )
        ) {
            /* NoticeDetailScreen(noticeId) */
        }

        composable("${Routes.MY_PROFILE}/${Routes.SETTING}/${Routes.BLOCKED_USERS}") {
            /* BlockedUserScreen() */
        }

        composable("${Routes.MY_PROFILE}/${Routes.UPDATE_PROFILE}") {
            UpdateProfileScreen(context = context, navController = navController)
        }

        composable("${Routes.TEAM_PROJECT}") {
            TeamProjectListScreen(navController=navController)
        }
        composable("${Routes.CHATTING}") {
            /* ChattingRoomListScreen(navController=navController) */
        }
    }
}