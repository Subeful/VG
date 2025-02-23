package com.andrey.vg.screens.main.group

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.LinearLayout.INVISIBLE
import android.widget.LinearLayout.VISIBLE
import android.widget.Toast
import androidx.collection.ArraySet
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.adapters.GroupAdapter
import com.andrey.vg.databinding.FragmentGroupsBinding
import com.andrey.vg.models.Groups
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.regex.Pattern


class GroupsFragment : Fragment() {

    lateinit var binding: FragmentGroupsBinding

    var listGroups = ArraySet<String>()
    lateinit var rv: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)

        rv = binding.rvChats
        setChats()

        return binding.root
    }

    fun setChats(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if(uid == null) return

        FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
            .addListenerForSingleValueEvent(object :
            ValueEventListener {
                var role = ""
                var group_id = ""
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupStr = snapshot.child("Users")
                if(groupStr.childrenCount.toInt() == 0)
                    return
                for (group in groupStr.children){
                    val groupID = group.child("group_id").value.toString()
                    if(groupID.length > 2)
                        listGroups.add(groupID)
                    if (uid == group.child("id").value.toString()){
                        role = group.child("role").value.toString()
                        group_id = group.child("group_id").value.toString()
                    }
                }
                if(role != "Student")
                    rv.setAdapter(GroupAdapter(context, listGroups.toList(), role));
                else
                    rv.setAdapter(GroupAdapter(context, listOf(group_id), role));
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(getContext(), "Failed to get user chats", Toast.LENGTH_SHORT).show();
            }
        })
    }
}