package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Adapters.FragmentAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding//view binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()//getting instance of auth

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    //inflating the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this@MainActivity)
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //using when condition on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.settings -> Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()

            R.id.logout -> {
                //logging out user and sending him/her to signup page
                auth.signOut()
                val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                this.finish()
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}