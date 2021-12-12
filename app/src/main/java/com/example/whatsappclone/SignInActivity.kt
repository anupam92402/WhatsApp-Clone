package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.databinding.ActivitySignInActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInActivityBinding
    private lateinit var alertDialog: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login")
        builder.setMessage("Logging to Your Account")

        binding.btnSignIn.setOnClickListener {
            alertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            alertDialog.dismiss()
            if (task.isSuccessful) {
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}