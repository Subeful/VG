package com.andrey.vg.screens.main.group

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArraySet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.andrey.vg.R
import com.andrey.vg.adapters.GroupAdapter
import com.andrey.vg.adapters.StudentAdapter
import com.andrey.vg.databinding.ActivityGroupInfoBinding
import com.andrey.vg.models.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.security.AccessController.getContext

class GroupInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroupInfoBinding
    var role = ""
    var group_id = ""
    lateinit var rv: RecyclerView
    val listStudent = ArraySet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupInfoBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        init()
        loadStudents()
    }

    fun init(){
        val intent = intent
        group_id = intent.getStringExtra("group_id").toString()
        binding.tvGroupName.text = group_id
        role = intent.getStringExtra("role").toString()
        rv = binding.rvStudents
    }

    fun loadStudents(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if(uid == null) return

        FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount.toInt() == 0)
                        return
                    for (group in snapshot.children){
                        if(group.child("group_id").value.toString() == group_id){
                            listStudent.add(group.child("userName").value.toString())
                        }
                            Log.d("MyLog", group.child("userName").value.toString())


                    }

                    rv.setAdapter(StudentAdapter(applicationContext, listStudent.toList()));
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Failed to get user chats", Toast.LENGTH_SHORT).show();
                }
            })
    }
}