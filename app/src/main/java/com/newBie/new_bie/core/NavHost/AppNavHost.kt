package com.newBie.new_bie.core.NavHost

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.presentation.screens.HomeScreen

@Composable
fun AppNavHost(modifier : Modifier, navController: NavHostController) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.SPLASH) {
            /* SplashScreen() */
        }

        composable(Routes.LOGIN) {
            /* LoginScreen() */
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
        ) {
            /* PostDetailScreen(id) */
        }

        composable(
            route = "${Routes.POST}/{id}/${Routes.POST_EDIT}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) {
            /* PostEditScreen(postId) */
        }

        // -------- User Profile --------
        composable(
            route = "${Routes.USER_PROFILE}/{userId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) {
            /* UserProfileScreen(userId) */
        }

        composable(
            route = "${Routes.USER_PROFILE}/{userId}/${Routes.FOLLOWER}?initialTab={initialTab}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("initialTab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            /* FollowerListScreen(targetUserId, initialTab) */
        }

        // -------- ShellRoute 대응 (BottomNav 영역) --------
        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable("${Routes.HOME}/${Routes.SEARCH}") {
            /* SearchResultScreen() */
        }

        composable(Routes.ADD) {
            /* PostAddScreen() */
        }

        composable(Routes.JOURNAL) {
            /* JournalScreen() */
        }

        composable(Routes.MY_PROFILE) {
            /* MyProfileScreen() */
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

        composable(
            route = "${Routes.MY_PROFILE}/${Routes.FOLLOWER}?initialTab={initialTab}",
            arguments = listOf(
                navArgument("initialTab") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            /* MyFollowerListScreen(initialTab) */
        }

        composable("${Routes.MY_PROFILE}/${Routes.UPDATE_PROFILE}") {
            /* UpdateProfileScreen() */
        }
    }
}