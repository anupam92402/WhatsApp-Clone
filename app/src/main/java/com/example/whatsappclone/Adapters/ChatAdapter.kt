package com.example.whatsappclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Models.MessageModel
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth


class ChatAdapter(private val messageList: ArrayList<MessageModel>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val senderViewType = 1
    private val receiverViewType = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == senderViewType) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sample_sender, parent, false)
            SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sample_receiver, parent, false)
            ReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessageModel = messageList[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            holder.senderMessage.text = currentMessageModel.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            holder.receiverMessage.text = currentMessageModel.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {

        return if (messageList[position].uid.equals(FirebaseAuth.getInstance().uid)) {
            senderViewType
        } else {
            receiverViewType
        }
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverMessage: TextView = itemView.findViewById(R.id.receiverText)
        val receiverTime: TextView = itemView.findViewById(R.id.receiverTime)
    }

    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessage: TextView = itemView.findViewById(R.id.senderText)
        val senderTime: TextView = itemView.findViewById(R.id.senderTime)
    }

}