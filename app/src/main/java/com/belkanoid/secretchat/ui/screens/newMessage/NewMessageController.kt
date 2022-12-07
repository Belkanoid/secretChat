package com.belkanoid.secretchat.ui.screens.newMessage

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.belkanoid.secretchat.presentation.Injector
import com.belkanoid.secretchat.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Composable
fun NewMessageController(
    navController: NavController,
    userId: String
) {

    val interactor = Injector.get().getInteractor()

    NewMessage(
        receiverId = userId,
        checkReceiver = {receiverId ->
            runBlocking(Dispatchers.IO) {
                interactor.getUser(receiverId)
            }
        }
    ) {receiver, message ->
        navController.navigate(Screen.MessagesScreen.route)
        interactor.sendMessage(receiver, message)
    }
}