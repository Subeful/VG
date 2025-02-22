package com.andrey.vg.screens

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.andrey.vg.R
import com.andrey.vg.databinding.ActivityMainBinding
import com.andrey.vg.screens.entrance.LoginActivity
import com.andrey.vg.screens.entrance.SignUpActivity
import com.andrey.vg.screens.main.group.GroupsFragment
import com.andrey.vg.screens.main.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var botNavMain: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        checkIsAuthorisation()

        init()

        botNavMain.setOnItemSelectedListener{it ->
            when(it.itemId){
                R.id.bot_menu_chats -> setFragment(GroupsFragment())
                R.id.bot_menu_profile -> setFragment(ProfileFragment())
            }
            true
        }

    }
    fun init(){
        setWindowsUtils()

        setFragment(GroupsFragment())
        botNavMain = binding.botNavMain
        botNavMain.itemActiveIndicatorColor = getColorStateList(R.color.transparent)
    }

    fun setWindowsUtils(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark))
    }

    fun checkIsAuthorisation(){
        if(FirebaseAuth.getInstance().currentUser == null){
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else{
            setStatusOnline()
        }
    }
    fun setStatusOnline(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("status").setValue("Online")
    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(binding.frameMain.id, fragment).commit()
    }

}