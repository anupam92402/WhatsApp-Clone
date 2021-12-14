package com.example.whatsappclone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.R

class UserAdapter(private val usersList: ArrayList<Users>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.IVProfileImage)
        val username: TextView = itemView.findViewById(R.id.tvUsername)
        val lastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = usersList[position]
        holder.username.text = currentUser.userName
        holder.lastMessage.text = currentUser.lastMessage
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}