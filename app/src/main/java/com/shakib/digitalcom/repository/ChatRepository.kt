package com.shakib.digitalcom.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.shakib.digitalcom.model.Chat
import com.shakib.digitalcom.utils.FirebaseEndpoints
import java.util.ArrayList

class ChatRepository {
    private val database = FirebaseDatabase.getInstance()
    private val messageReference = database.getReference(FirebaseEndpoints.MESSAGE)

    companion object{
        private val instance = ChatRepository()
        fun getInstance(): ChatRepository = instance
    }

    fun getChatMessages(): LiveData<Chat>{

        val chatData = MutableLiveData<Chat>()

        messageReference.addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chat: Chat? = dataSnapshot.getValue(Chat::class.java)
                chatData.value = chat
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
        return chatData
    }
}