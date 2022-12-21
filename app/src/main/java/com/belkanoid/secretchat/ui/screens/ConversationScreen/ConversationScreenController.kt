package com.belkanoid.secretchat.ui.screens.ConversationScreen

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.presentation.Injector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        withContext(Dispatchers.IO) {
            interactor.startCompanionRefreshJob(companionId = companionUserId.toLong())
            Log.d("MQSS on startCompRe", "${conversationMessages.size}")
        }
    }

    interactor.initialized.observe(lifecycleOwner) {
        val allMessages = interactor.messages.value
        conversationMessages = emptyList()
        companionMessages =
            allMessages?.filter { it.sender == companionUserId.toLong() } ?: listOf()
    }

    interactor.companionInitialized.observe(lifecycleOwner) {
        userSelfMessages =
            interactor.companionMessages.value?.filter { it.sender == interactor.getUserId() }
                ?: listOf()
        conversationMessages = emptyList()
        conversationMessages = userSelfMessages + companionMessages
        conversationMessages = conversationMessages.sortedBy { it.timestamp }
        Log.d("MQSS on start2", "${conversationMessages.size}")
    }

    ConversationScreen(
        companionId = companionUserId.toLong(),
        conversationMessages = conversationMessages.toHashSet().toList(),
    ) { text ->
        interactor.sendMessage(companionUserId.toLong(), text)
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            interactor.companionInitialized.removeObservers(lifecycleOwner)
            interactor.initialized.removeObservers(lifecycleOwner)
            interactor.stopCompanionRefreshJob()
            conversationMessages = emptyList()
        }
    }
}

