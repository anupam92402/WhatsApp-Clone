package com.example.whatsappclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Models.MessageModel
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

//sending and receiving messages adapter

class GroupChatAdapter(private val messageList: ArrayList<MessageModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val senderViewType = 1
    private val receiverViewType = 2
    private var username: String? = "username"

    //inflating the layout depending on the uid
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == senderViewType) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sample_sender, parent, false)
            SenderViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.sample_receiver_group_chat, parent, false)
            GroupReceiverViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessageModel = messageList[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            holder.senderMessage.text = currentMessageModel.message
        } else {
            val viewHolder = holder as GroupReceiverViewHolder
            holder.receiverMessage.text = currentMessageModel.message
            getUserName(currentMessageModel)
            holder.receiverName.text = username
            val colorArray = context.resources.getIntArray(R.array.random_color)
            val color = colorArray[position % 14]
            holder.receiverName.setTextColor(color)
        }
    }

    private fun getUserName(model: MessageModel) {

        val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        mDbRef.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapShot in snapshot.children) {
                    val user = dataSnapShot.getValue(Users::class.java)

                    if (model.uid.equals(user?.userId)) {
                        username = user?.userName
                        break
                    }
                    notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
    class GroupReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiverName: TextView = itemView.findViewById(R.id.groupChatReceiverName)
        val receiverMessage: TextView = itemView.findViewById(R.id.groupChatReceiverText)
        val receiverTime: TextView = itemView.findViewById(R.id.groupChatReceiverTime)
    }

    //sender view holder
    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessage: TextView = itemView.findViewById(R.id.senderText)
        val senderTime: TextView = itemView.findViewById(R.id.senderTime)
    }

}