package com.example.whatsappclone.Adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Models.MessageModel
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

//sending and receiving messages adapter

class ChatAdapter(
    private val messageList: ArrayList<MessageModel>,
    val context: Context,
    val recvId: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val senderViewType = 1
    private val receiverViewType = 2

    //inflating the layout depending on the uid
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

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure , you want to delete the message")
                .setPositiveButton("Yes") { dialogInterface, which ->
                    val mDBRef = FirebaseDatabase.getInstance().reference
                    val senderRoom = FirebaseAuth.getInstance()
                    mDBRef.child("chats")
                        .child(currentMessageModel.messageId)
                        .setValue(null)
                }.setNegativeButton("No") { dialogInterface, which ->
                    dialogInterface.dismiss()
                }.show()
            false
        }

        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            holder.senderMessage.text = currentMessageModel.message
        } else {
            val viewHolder = holder as ReceiverViewHolder
            holder.receiverMessage.text = currentMessageModel.message
        }
    }

    //return size of list
    override fun getItemCount(): Int {
        return messageList.size
    }


    //to check that current uid matches to sender or receiver
    override fun getItemViewType(position: Int): Int {

        return if (messageList[position].uid.equals(FirebaseAuth.getInstance().uid)) {
            senderViewType
        } else {
            receiverViewType
        }
    }

    //receiver view holder
    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverMessage: TextView = itemView.findViewById(R.id.receiverText)
        val receiverTime: TextView = itemView.findViewById(R.id.receiverTime)
    }

    //sender view holder
    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessage: TextView = itemView.findViewById(R.id.senderText)
        val senderTime: TextView = itemView.findViewById(R.id.senderTime)
    }

}