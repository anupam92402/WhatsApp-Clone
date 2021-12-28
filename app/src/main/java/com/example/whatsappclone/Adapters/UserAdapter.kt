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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

//adapter class to display user

class UserAdapter(private val usersList: ArrayList<Users>, private val context: Context) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    //inflating sample show user layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sample_show_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getting current user from array list
        val currentUser = usersList[position]
        holder.username.text = currentUser.userName
        if (!currentUser.profilePic.equals("")) {
            Picasso.get().load(currentUser.profilePic).placeholder(R.drawable.defaultprofile)
                .into(holder.image)
        }

        FirebaseDatabase.getInstance().reference.child("chats")
            .child(FirebaseAuth.getInstance().uid + currentUser.userId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (dataSnapShot in snapshot.children) {
                            holder.lastMessage.text =
                                dataSnapShot.child("message").getValue().toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        if (holder.lastMessage.text.equals("Last Message")) {
            holder.lastMessage.text = ""
        }

        //on Click listener on layout when user tap on any other user
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