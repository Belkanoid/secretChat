package com.belkanoid.secretchat.ui.navigation

sealed class Screen(val route: String) {
    object MessagesScreen: Screen("messages_route")
    object NewMessageScreen: Screen("new_message_route")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg->
                append("/$arg")
            }
        }
    }
}
