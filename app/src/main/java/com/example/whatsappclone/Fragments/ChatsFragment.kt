package com.example.whatsappclone.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapters.UserAdapter
import com.example.whatsappclone.Models.Users
import com.example.whatsappclone.databinding.FragmentChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private var list = ArrayList<Users>()
    private lateinit var mDbRef: DatabaseReference
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        //auth
        auth = FirebaseAuth.getInstance()

        //setting recycler view
        adapter = UserAdapter(list, requireContext())
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)

        //getting users from firebase
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()//clear old data
                for (dataSnapShot in snapshot.children) {
                    val user = dataSnapShot.getValue(Users::class.java)
                    user?.userId = dataSnapShot.key.toString()
                    //hiding current logged in user
                    if (auth.currentUser?.uid == user?.userId) {
                        continue
                    }
                    list.add(user!!)
                }
                adapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error:- ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
        return binding.root
    }


}

