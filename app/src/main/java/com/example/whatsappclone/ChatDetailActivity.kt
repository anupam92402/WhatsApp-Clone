package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.databinding.ActivityChatDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatDetailBinding//view binding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

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

        val senderId = auth.uid//sender Id
        val receiverId = intent.getStringExtra("userId")//receiver Id
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

    }
}
