package com.belkanoid.secretchat.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.belkanoid.secretchat.data.repository.MessengerRepositoryImpl
import com.belkanoid.secretchat.data.util.SharedPreferences
import com.belkanoid.secretchat.data.util.SharedPreferences.Companion.USER_ID
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: MessengerRepositoryImpl,
    private val sharedPreferences : SharedPreferences
    ) : ViewModel() {

    private var userId = -1L

    private val _queue: MutableLiveData<List<Queue>> = MutableLiveData()
    private val _messageList: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messageList

    private val _userCreationError: MutableLiveData<Unit> = MutableLiveData()
    val userCreationError: LiveData<Unit> = _userCreationError

    private val _needToCreateUser: MutableLiveData<Unit> = MutableLiveData()
    val needToCreateUser: LiveData<Unit> = _needToCreateUser

    init {
        initApp()
    }

    private fun initApp() {
        userId = 0//sharedPreferences.getLong(name = USER_ID)
        if (userId == -1L) {
            _needToCreateUser.postValue(Unit)
        }
        else {
            initQueue(userId)
        }
    }

    private fun initQueue(userId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val queue = repository.getQueue(userId = userId)
                _queue.postValue(queue)
                Log.d("MESS InitQueue t", queue.toString())
                initMessageList(queue)
            } catch (e: Exception) {
                Log.d("InitQueue", e.toString())
            }
        }
    }

    private fun initMessageList(queue: List<Queue>) {
        val messages = mutableListOf<Message>()
        for (it in queue) {
            val messageId = it.messageId.toLong()
            viewModelScope.launch(Dispatchers.IO) {
                val message = repository.getMessage(messageId = messageId)
                messages.add(message)
                _messageList.postValue(messages)
            }
        }
    }

    fun sendMessage(receiver: Long, message: String, isEdited: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessage(
                receiver = receiver,
                sender = userId,
                message = message,
                isEdited = isEdited
            )
        }
    }

    fun createUser(userName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val userId = repository.createUser(userName = userName)
            if (userId == -1L) {
                _userCreationError.postValue(Unit)
            }
            sharedPreferences.putLong(value = userId)
        }
    }

    fun refreshQueue() {
        initQueue(userId)
    }

}