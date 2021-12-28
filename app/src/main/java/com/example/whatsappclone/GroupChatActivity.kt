package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapters.GroupChatAdapter
import com.example.whatsappclone.Models.MessageModel
import com.example.whatsappclone.databinding.ActivityGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

//group chat functionality

class GroupChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupChatBinding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var messageList: ArrayList<MessageModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var chatAdapter: GroupChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing action bar
        supportActionBar?.hide()

        //getting instance of auth
        auth = FirebaseAuth.getInstance()

        //getting database reference
        mDbRef = FirebaseDatabase.getInstance().reference

        messageList = ArrayList()

        val senderId = auth.currentUser?.uid//sender Id (current signed in user)

        //chat recycler view adapter
        chatAdapter = GroupChatAdapter(messageList, this)
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        //on click listener on back button
        binding.btnBack.setOnClickListener {
            val intent = Intent(this@GroupChatActivity, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        mDbRef.child("Group Chat").addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()

                for (postSnapshot in snapshot.children) {
                    val model = postSnapshot.getValue(MessageModel::class.java)
                    messageList.add(model!!)
                }

                chatAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error:- ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        )

        //on click on send button
        binding.send.setOnClickListener {

            val message = binding.etMessage.text.toString()
            val model = MessageModel(senderId!!, message)
            model.timeStamp = Date().time

            binding.etMessage.setText("")//once user send message clear edit text

            if (message.trim().isNotEmpty()) {
                mDbRef.child("Group Chat").push()
                    .setValue(model).addOnSuccessListener { it: Void? ->

                    }
            }

        }

    }
}