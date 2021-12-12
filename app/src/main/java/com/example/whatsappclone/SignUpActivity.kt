package com.example.whatsappclone

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Creating Account")
        builder.setMessage("We are Creating Your Account")

        binding.btnSignUp.setOnClickListener {

            alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

            val userName = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            signUp(userName, email, password)

        }
    }

    private fun signUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                alertDialog.dismiss()
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, password, auth.currentUser?.uid!!)
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, password: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("Users").child(uid).setValue(Users(name, email, password))
    }
}