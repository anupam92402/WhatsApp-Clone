package com.example.whatsappclone.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.ChatDetailActivity
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.R
import com.squareup.picasso.Picasso

//adapter class of displaying user

class UserAdapter(private val usersList: ArrayList<Users>, private val context: Context) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    //inflating layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getting current user from array list
        val currentUser = usersList[position]
        holder.username.text = currentUser.userName
//        holder.lastMessage.text = currentUser.lastMessage
        if (!currentUser.profilePic.equals("")) {
            Picasso.get().load(currentUser.profilePic).placeholder(R.drawable.defaultprofile)
                .into(holder.image)
        }
        //on Click listener on layout
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatDetailActivity::class.java)
            intent.putExtra("userId", currentUser.userId)
            intent.putExtra("profilePic", currentUser.profilePic)
            intent.putExtra("userName", currentUser.userName)
            context.startActivity(intent)
        }
    }

    //returning size of user list
    override fun getItemCount(): Int {
        return usersList.size
    }

    //initializing variables of layout file
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.IVProfileImage)
        val username: TextView = itemView.findViewById(R.id.tvUsername)
        val lastMessage: TextView = itemView.findViewById(R.id.tvLastMessage)
    }
}