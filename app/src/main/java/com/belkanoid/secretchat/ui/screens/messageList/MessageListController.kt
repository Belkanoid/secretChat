package com.belkanoid.secretchat.ui.screens.messageList

import android.util.Log
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.belkanoid.secretchat.di.DaggerMessengerComponent
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.User
import com.belkanoid.secretchat.presentation.Injector
import com.belkanoid.secretchat.ui.dialogs.CreateUserDialog
import com.belkanoid.secretchat.ui.dialogs.ErrorDialog
import com.belkanoid.secretchat.ui.navigation.Screen

@Composable
fun MessageListController(
    navController: NavController
) {
    val interactor = Injector.get().getInteractor()

    var messages by remember { mutableStateOf(listOf<Message>())}
    var users by  remember { mutableStateOf(listOf<User>())}


    interactor.initialized.observe(LocalLifecycleOwner.current){
        messages = interactor.messages.value ?: listOf()
        users = interactor.users.value?.toList() ?: listOf()
    }
    val showCreateUserDialog = remember { mutableStateOf(false) }

    interactor.needToCreateUser.observe(LocalLifecycleOwner.current) {
        if (interactor.getUserId() == -1L) {
            showCreateUserDialog.value = true
            Log.d("MESS on need to create", "${interactor._userId}")
        }
    }

    Surface() {
        if(showCreateUserDialog.value) {
            CreateUserDialog(onDismissRequest = {
                showCreateUserDialog.value = false
            }){ userName ->
            interactor.createUser(userName)
            }
        }
    }


    MessageList(
        messages = messages,
        users = users,
        onNewMessage = {
            navController.navigate(Screen.NewMessageScreen.withArgs("0"))
        },
        onReplayButton = { userId ->
            navController.navigate(Screen.NewMessageScreen.withArgs(userId))
        },
        onOpenMessageClick = {

        }
    )



}