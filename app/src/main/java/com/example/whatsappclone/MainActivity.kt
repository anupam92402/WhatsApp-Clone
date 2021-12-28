package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.whatsappclone.Adapters.FragmentAdapter
import com.example.whatsappclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding//view binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //view binding
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getting instance of auth
        auth = FirebaseAuth.getInstance()

        //attaching tab layout with view pager
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

            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }

            R.id.grpchat -> {
                val intent = Intent(this@MainActivity, GroupChatActivity::class.java)
                startActivity(intent)
            }

            R.id.logout -> {
                //logging out user and sending him/her to signup page
                auth.signOut()
                val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}