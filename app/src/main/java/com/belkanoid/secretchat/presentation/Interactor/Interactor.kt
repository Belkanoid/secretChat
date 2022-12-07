package com.belkanoid.secretchat.presentation.Interactor

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.belkanoid.secretchat.data.repository.MessengerRepositoryImpl
import com.belkanoid.secretchat.data.util.SharedPreferences
import com.belkanoid.secretchat.data.util.SharedPreferences.Companion.USER_ID
import com.belkanoid.secretchat.domain.entity.Message
import com.belkanoid.secretchat.domain.entity.Queue
import com.belkanoid.secretchat.domain.entity.User
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext


@Singleton
class Interactor @Inject constructor(
    private val repository: MessengerRepositoryImpl,
    private val sharedPreferences: SharedPreferences,
) {

    var refreshQueueJob: Job? = null

    var _userId = sharedPreferences.getLong(name = USER_ID)
        private set


    fun saveUserId(userId: Long) {
        sharedPreferences.putLong(value = userId)
        _userDefined.postValue(Unit)
    }

    fun getUserId(): Long {
        return sharedPreferences.getLong()
    }

    private val _queue: MutableLiveData<List<Queue>> = MutableLiveData()

    private val _messageList: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messageList

    private val _userList: MutableLiveData<MutableSet<User>> = MutableLiveData(mutableSetOf())
    val users: LiveData<MutableSet<User>> = _userList

    val initialized: MutableLiveData<Unit> = MutableLiveData()

    private val _userDefined: MutableLiveData<Unit> = MutableLiveData()
    val userDefined: LiveData<Unit> = _userDefined

    private val _needToCreateUser: MutableLiveData<Unit> = MutableLiveData()
    val needToCreateUser: LiveData<Unit> = _needToCreateUser


    init {
        runBlocking(Dispatchers.IO) {
            initApp()
        }
    }

    private fun initApp() {
        if (getUserId()== -1L) {
            _needToCreateUser.postValue(Unit)
        } else {
            _userDefined.postValue(Unit)
            refreshQueueJob = refreshQueue()
        }
    }

    private suspend fun initQueue(userId: Long) {
        withContext(Dispatchers.IO) {
            try {
                val queue = repository.getQueue(userId = userId)
                _queue.postValue(queue)
                Log.d("MESS InitQueue t", queue.toString())
                initMessageList(queue)
            } catch (e: Exception) {
                Log.d("MESS InitQueue", e.toString())
            }
        }
        initialized.postValue(Unit)
    }

    private suspend fun initMessageList(queue: List<Queue>) {
        val messages = mutableListOf<Message>()
        for (it in queue) {
            val messageId = it.messageId.toLong()
            withContext(Dispatchers.IO) {
                val message = repository.getMessage(messageId = messageId)
                initUserList(message.sender)
                messages.add(message)
            }
        }
        _messageList.postValue(messages)
    }

    private suspend fun initUserList(userId: Long) {
        val user = repository.getUser(userId = userId)
        _userList.value!!.add(user)
        _userList.postValue(_userList.value)
    }


    suspend fun getUser(userId: Long = getUserId()): User {
        return repository.getUser(userId = userId)

    }

    fun sendMessage(receiver: Long, message: String, isEdited: Boolean = false) {
        runBlocking(Dispatchers.IO) {
            repository.sendMessage(
                receiver = receiver,
                sender = getUserId(),
                message = message,
                isEdited = isEdited
            )
        }
    }

    fun createUser(userName: String) {
        runBlocking(Dispatchers.IO) {
            repository.createUser(userName = userName)
        }
    }

    fun getUserList(userListId: List<Long>) {
        val users = mutableSetOf<User>()
        for (userId in userListId) {
            runBlocking(Dispatchers.IO) {
                val user = repository.getUser(userId)
                users.add(user)
                _userList.postValue(users)
            }
        }
    }

    fun refreshQueue(): Job {
        val userId = getUserId()
        return CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                initQueue(userId)
                delay(5000L)
            }
        }
    }

}