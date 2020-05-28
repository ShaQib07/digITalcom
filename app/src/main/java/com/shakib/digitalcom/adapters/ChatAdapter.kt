package com.shakib.digitalcom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shakib.digitalcom.R
import com.shakib.digitalcom.model.Chat
import kotlinx.android.synthetic.main.chat_row.view.*

class ChatAdapter(private val mContext: Context, private val mChatList: List<Chat>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(mChat: Chat, position: Int){
            if (position % 2 == 0){
                itemView.txt_chat_me.visibility = View.VISIBLE
                itemView.txt_chat_others.visibility = View.INVISIBLE
                itemView.txt_chat_me.text = mChat.chat
            } else{
                itemView.txt_chat_others.visibility = View.VISIBLE
                itemView.txt_chat_me.visibility = View.INVISIBLE
                itemView.txt_chat_others.text = mChat.chat
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.chat_row, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mChatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val mChat = mChatList[position]
        holder.setData(mChat, position)
    }
}