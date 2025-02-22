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
        setName()
//        loadAva()
    }

    fun setName(){
        FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvProfileUsername.setText(snapshot.child("userName").value.toString())
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

/*    fun chooseAva(inflater: LayoutInflater){
        val view = LayoutInflater.from(inflater.context).inflate(R.layout.alert_choose_ava, null)

        val im_1 = view.findViewById<ImageButton>(R.id.alert_choose_1)
        val im_2 = view.findViewById<ImageButton>(R.id.alert_choose_3)
        val im_3 = view.findViewById<ImageButton>(R.id.alert_choose_2)
        val im_4 = view.findViewById<ImageButton>(R.id.alert_choose_4)

        im_1.setOnClickListener { setAva(1) }
        im_2.setOnClickListener { setAva(2) }
        im_3.setOnClickListener { setAva(3) }
        im_4.setOnClickListener { setAva(4) }

        AlertDialog.Builder(inflater.context).setView(view).create().show()
    }
    fun setAva(imageId: Int){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profileImage").setValue(imageId)

        when(imageId){
            1 -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_1)
            2 -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_2)
            3 -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_3)
            4 -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_4)
        }
    }
    fun loadAva(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profileImage").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    when(snapshot.value.toString()){
                        "1" -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_1)
                        "2" -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_2)
                        "3" -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_3)
                        "4" -> binding.imProfileAva.setBackgroundResource(R.drawable.png_animal_4)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }*/
}
