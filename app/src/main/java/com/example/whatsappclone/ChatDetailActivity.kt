package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapters.ChatAdapter
import com.example.whatsappclone.Models.MessageModel
import com.example.whatsappclone.databinding.ActivityChatDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding//view binding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var messageList: ArrayList<MessageModel>
    private lateinit var chatAdapter: ChatAdapter
    private var senderRoom: String? = null
    private var receiverRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //view binding
        super.onCreate(savedInstanceState)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing action bar
        supportActionBar?.hide()

        //getting instance of auth
        auth = FirebaseAuth.getInstance()

        //getting database reference
        mDbRef = FirebaseDatabase.getInstance().reference

        messageList = ArrayList()

        val senderId = auth.currentUser?.uid//sender Id
        val receiverId = intent.getStringExtra("userId")//receiver Id

        senderRoom = senderId + receiverId
        receiverRoom = receiverId + senderId

        //getting intent extra
        val userName = intent.getStringExtra("userName")
        val profilePic = intent.getStringExtra("profilePic")

        //putting intent extra values in views
        binding.username.text = userName
        if (!profilePic.equals("")) {
            Picasso.get().load(profilePic).placeholder(R.drawable.defaultprofile)
                .into(binding.IVProfileImage)
        }

        //on click listener on back button
        binding.btnBack.setOnClickListener {
            val intent = Intent(this@ChatDetailActivity, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        //chat recycler view adapter
        chatAdapter = ChatAdapter(messageList, this)
        binding.chatRecyclerView.adapter = chatAdapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        mDbRef.child("chats")
            .child(senderRoom!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapshot in snapshot.children) {
                        val model = postSnapshot.getValue(MessageModel::class.java)
                        messageList.add(model!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //on click on send button
        binding.send.setOnClickListener {

            val message = binding.etMessage.text.toString()
            val model = MessageModel(senderId!!, message)

            model.timeStamp = Date().time
            binding.etMessage.setText("")

            if (message.trim().isNotEmpty()) {
                mDbRef.child("chats").child(senderRoom!!).push()
                    .setValue(model).addOnSuccessListener { it: Void? ->
                        mDbRef.child("chats").child(receiverRoom!!).push()
                            .setValue(model)
                    }
            }

        }

    }
}
