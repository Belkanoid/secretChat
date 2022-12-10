package com.belkanoid.secretchat.ui.screens.ConversationScreen

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.presentation.Injector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ConversationScreenController(
    navController: NavController,
    companionUserId: String,
) {
    val interactor = Injector.get().getInteractor()

    var companionMessages by remember { mutableStateOf(listOf<Message>()) }
    var userSelfMessages by remember { mutableStateOf(listOf<Message>()) }
    var conversationMessages by remember { mutableStateOf(listOf<Message>()) }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        launch(Dispatchers.IO) {
            interactor.startCompanionRefreshJob(companionId = companionUserId.toLong())
            Log.d("MESS on startCompRe", "olo")
        }
    }

    interactor.initialized.observe(lifecycleOwner) {
        val allMessages = interactor.messages.value
        companionMessages = allMessages?.filter { it.sender == companionUserId.toLong() } ?: listOf()
    }

    interactor.companionInitialized.observe(lifecycleOwner) {
        userSelfMessages = interactor.companionMessages.value ?: listOf()
        conversationMessages = userSelfMessages + companionMessages
        conversationMessages = conversationMessages.sortedBy { it.timestamp }
    }

    ConversationScreen(
        companionId = companionUserId.toLong(),
        conversationMessages = conversationMessages.toList(),
    ) { text ->
        interactor.sendMessage(companionUserId.toLong(), text)
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            interactor.companionInitialized.removeObservers(lifecycleOwner)
            interactor.initialized.removeObservers(lifecycleOwner)
            interactor.stopCompanionRefreshJob()
        }
    }
}

