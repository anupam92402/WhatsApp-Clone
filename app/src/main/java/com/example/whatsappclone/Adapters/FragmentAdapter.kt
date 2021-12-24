package com.example.whatsappclone.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsappclone.Fragments.CallsFragment
import com.example.whatsappclone.Fragments.ChatsFragment
import com.example.whatsappclone.Fragments.StatusFragment

class FragmentAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    //total number of fragments
    override fun getCount(): Int {
        return 3
    }

    // creating instance of fragment according to position
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            1 -> StatusFragment()
            2 -> CallsFragment()
            else ->
                ChatsFragment()//default fragment
        }
    }

    //title of each fragment
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "CHATS"
            1 -> "STATUS"
            2 -> "CALLS"
            else -> "CHATS"//default title
        }
    }
}