package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//when the user needs to register to the app

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        //view binding
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing action bar
        supportActionBar?.hide()

        //getting instance of auth
        auth = FirebaseAuth.getInstance()

        //setting alert dialog between sign in/sign up task
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Creating Account")
        builder.setMessage("We are Creating Your Account")

        //redirecting user to SignIn Page
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            this.finish()
            startActivity(intent)
        }

        //when user trying the sign up
        binding.btnSignUp.setOnClickListener {

            //taking data inputted by user
            val userName = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            //checking inputted data is not null
            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                displayWarning(userName, email, password)
            } else {
                //displaying the alert dialog
                alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

                signUp(userName, email, password)
            }

        }
    }

    //display warning when any of the field is empty
    private fun displayWarning(userName: String, email: String, password: String) {
        if (userName.isEmpty()) {
            binding.etUsername.error = "Name can't be empty"
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Email Id can't be empty"
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password can't be empty"
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                alertDialog.dismiss()//close the alert dialog
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, password, auth.currentUser?.uid!!)
                    //sign up is successful sending user to main activity
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    this.finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    //add the user to firebase database
    private fun addUserToDatabase(name: String, email: String, password: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference//getting db reference
        //creating the child of user
        mDbRef.child("Users").child(uid).setValue(Users(name, email, password, uid))
    }
}