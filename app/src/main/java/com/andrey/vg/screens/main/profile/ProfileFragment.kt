package com.andrey.vg.screens.main.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.andrey.vg.R
import com.andrey.vg.databinding.FragmentProfileBinding
import com.andrey.vg.screens.entrance.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var alert: AlertDialog.Builder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        init()

        binding.ibProfileLogOut.setOnClickListener { logout(container) }
//        binding.imProfileAva.setOnClickListener { chooseAva(inflater) }

        return binding.root
    }

    fun init(){
        setInfo()
//        loadAva()
    }

    fun setInfo(){
        FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvProfileUsername.setText(snapshot.child("userName").value.toString())
                    val role = snapshot.child("role").value.toString()

                    binding.tvProfileInfo.text = when(role){
                        "Scheduler" -> "Составитель расписаний"
                        "Teacher" -> "Преподаватель"
                        else -> {
                            "Студент группы " + snapshot.child("group_id").value.toString()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun logout(container: ViewGroup?){
        alert = AlertDialog.Builder(container!!.context)
            .setTitle("${getString(R.string.logout)}?")
            .setPositiveButton(getString(R.string.yes), object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                }
            }).setNegativeButton(getString(R.string.no), object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    return
                }
            })
        alert.create().show()
    }

}
