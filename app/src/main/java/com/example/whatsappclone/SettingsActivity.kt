package com.example.whatsappclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mDbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing action bar
        supportActionBar?.hide()

        //getting instance of auth
        auth = FirebaseAuth.getInstance()

        //getting database reference
        mDbRef = FirebaseDatabase.getInstance().reference

        //getting storage instance
        storage = FirebaseStorage.getInstance()

        //on click listener on back button
        binding.backArrow.setOnClickListener {
            val intent = Intent(this@SettingsActivity, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        binding.btnSave.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val status = binding.etStatus.text.toString()

            val map: HashMap<String, String> = HashMap()
            map.put("userName", username)
            map.put("about", status)
            mDbRef.child("Users").child(auth.uid!!).updateChildren(map as Map<String, Any>)
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
        }

        mDbRef.child("Users").child(auth.uid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(Users::class.java)
                    if (!user?.profilePic.equals("")) {
                        Picasso.get().load(user?.profilePic).placeholder(R.drawable.defaultprofile)
                            .into(binding.profileImage)
                    }
                    binding.etStatus.setText(user?.about)
                    binding.etUsername.setText(user?.userName)
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //on click listener on back button
        binding.plus.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 33)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data?.data != null) {
            val sFile: Uri = data.data!!
            binding.profileImage.setImageURI(sFile)

            val reference = storage.reference.child("profile_pictures")
                .child(auth.uid!!)

            reference.putFile(sFile).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener {
                    mDbRef.child("Users").child(auth.uid!!).child("profilePic")
                        .setValue(it.toString())
                }
            }
        }
    }
}