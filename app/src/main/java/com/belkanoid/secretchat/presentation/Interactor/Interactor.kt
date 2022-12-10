package com.belkanoid.secretchat.presentation.Interactor

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


@Singleton
class Interactor @Inject constructor(
    private val repository: MessengerRepositoryImpl,
    private val sharedPreferences: SharedPreferences,
) {

    private var refreshQueueJob: Job? = null
    private var refreshCompanionQueueJob: Job? = null

    fun startRefreshJob() {
        refreshQueueJob = refreshQueue()
    }

    fun stopRefreshJob() {
        refreshQueueJob?.cancel()
    }

    fun startCompanionRefreshJob(companionId: Long) {
        refreshCompanionQueueJob = refreshCompanionQueue(companionId)
    }

    fun stopCompanionRefreshJob() {
        refreshCompanionQueueJob?.cancel()
    }

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
    private val _companionQueue: MutableLiveData<List<Queue>> = MutableLiveData()

    private val _messageList: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>> = _messageList

    private val _companionMessageList: MutableLiveData<List<Message>> = MutableLiveData()
    val companionMessages: LiveData<List<Message>> = _companionMessageList

    private val _userList: MutableLiveData<MutableSet<User>> = MutableLiveData(mutableSetOf())
    val users: LiveData<MutableSet<User>> = _userList

    val initialized: MutableLiveData<Unit> = MutableLiveData()
    val companionInitialized: MutableLiveData<Unit> = MutableLiveData()

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
        if (getUserId() == -1L) {
            _needToCreateUser.postValue(Unit)
        } else {
            _userDefined.postValue(Unit)
            startRefreshJob()
        }
    }

    suspend fun initCompanionQueue(companionId: Long) {
        withContext(Dispatchers.IO) {
            try {
                val queue = repository.getQueue(userId = companionId)
                _companionQueue.postValue(queue)
                Log.d("MESS CompInitQueue size", queue.size.toString())
                initCompanionMessageList(queue)
            } catch (e: Exception) {
                Log.d("MESS CompanionInitQueue", e.toString())
            }
        }
        companionInitialized.postValue(Unit)
    }

    private suspend fun initCompanionMessageList(queue: List<Queue>) {
        val messages = mutableListOf<Message>()
        for (it in queue) {
            val messageId = it.messageId.toLong()
            withContext(Dispatchers.IO) {
                val message = repository.getMessage(messageId = messageId)
                messages.add(message)
            }
        }
        _companionMessageList.postValue(messages)
    }

    private suspend fun initQueue(userId: Long) {
        withContext(Dispatchers.IO) {
            try {
                val queue = repository.getQueue(userId = userId)
                _queue.postValue(queue)
                Log.d("MESS UInitQueue size", queue.size.toString())
                initMessageList(queue)
            } catch (e: Exception) {
                Log.d("MESS UInitQueue", e.toString())
            }
        }
        initialized.postValue(Unit)
        Log.d("MASS", "init")
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


    private fun refreshQueue(): Job {
        val userId = getUserId()
        return CoroutineScope(Dispatchers.IO).launch {
            delay(3000L)
            while (refreshQueueJob?.isCancelled == false) {
                initQueue(userId)
            }
        }
    }

    private fun refreshCompanionQueue(companionId: Long): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            delay(3000L)
            while (refreshCompanionQueueJob?.isCancelled == false) {
                initCompanionQueue(companionId)
            }
        }
    }

}