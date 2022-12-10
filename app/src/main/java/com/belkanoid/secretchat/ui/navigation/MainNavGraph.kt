package com.belkanoid.secretchat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.belkanoid.secretchat.ui.screens.ConversationScreen.ConversationScreenController
import com.belkanoid.secretchat.ui.screens.messageList.MessageListController
import com.belkanoid.secretchat.ui.screens.newMessage.NewMessageController

@Composable
fun MainNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.MessagesScreen.route) {
        composable(Screen.MessagesScreen.route){
            MessageListController(navController)
        }
        composable(
            route = Screen.NewMessageScreen.route + "/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
            NewMessageController(navController, userId = it.arguments?.getString("userId")?: "")
        }
        composable(
            route = Screen.ConversationScreen.route + "/{companionUserId}",
            arguments = listOf(
                navArgument("companionUserId") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
            ConversationScreenController(navController, companionUserId = it.arguments?.getString("companionUserId")?: "")
        }
    }
}
