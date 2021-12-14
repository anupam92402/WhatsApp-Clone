package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//when user logging to already existing account

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding//view binding
    private lateinit var alertDialog: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient//for sign in via google
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing action bar
        supportActionBar?.hide()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()

        auth = FirebaseAuth.getInstance()//getting instance of auth

        mDbRef = FirebaseDatabase.getInstance().reference

        //if user is already logged in redirecting him/her to main activity
        if (auth.currentUser != null) {
            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            this.finish()
            startActivity(intent)
        }

        //when user tap on google button
        binding.btnGoogle.setOnClickListener {
            signIn()
        }

        //setting alert dialog between sign in/sign up task
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login")
        builder.setMessage("Logging to Your Account")

        //when user trying the sign in
        binding.btnSignIn.setOnClickListener {
            //taking data inputted by user
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            //checking inputted data is not null
            if (email.isEmpty() || password.isEmpty()) {
                displayWarning(email, password)
            } else {
                //displaying the alert dialog
                alertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                signIn(email, password)
            }
        }

        //redirecting user to Sign Up Activity
        binding.tvRegister.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            this.finish()
            startActivity(intent)
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            alertDialog.dismiss()
            if (task.isSuccessful) {
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                this.finish()
                startActivity(intent)
            } else {
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //display warning when any of the field is empty
    private fun displayWarning(email: String, password: String) {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email Id can't be empty"
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Password can't be empty"
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val currentUser = auth.currentUser!!
                    val users = Users()
                    users.userId = currentUser.uid
                    users.userName = currentUser.displayName!!
                    users.profilePic = currentUser.photoUrl.toString()
                    mDbRef.child("Users").child(currentUser.uid).setValue(users)
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    startActivity(intent)
                    this.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

}