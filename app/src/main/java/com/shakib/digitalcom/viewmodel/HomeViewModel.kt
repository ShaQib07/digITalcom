package com.shakib.digitalcom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shakib.digitalcom.model.Chat
import com.shakib.digitalcom.repository.ChatRepository

class HomeViewModel: ViewModel() {

    private val chatRepository = ChatRepository.getInstance()
    private var chatMessage = MutableLiveData<Chat>()

    fun getChatMessages(): LiveData<Chat>{
        chatRepository.getChatMessages().observeForever {
            chatMessage.value = it
        }
        return chatMessage
    }
}