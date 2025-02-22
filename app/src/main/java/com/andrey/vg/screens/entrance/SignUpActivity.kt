package com.andrey.vg.screens.entrance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.andrey.vg.R
import com.andrey.vg.databinding.ActivitySignUpBinding
import com.andrey.vg.models.Users
import com.andrey.vg.screens.MainActivity
import com.andrey.vg.utils.Cipher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var context: Context

    var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        binding.btSignUpSignUp.setOnClickListener {
            val email = binding.etSignUpEmail.text.toString().trim()
            val username = binding.etSignUpUsername.text.toString().trim()
            val temporarilyPassword = binding.etSignUpPassword.text.toString().trim()
            val password = Cipher.encryptPassword(context, temporarilyPassword)

            if(email.isEmpty() || password.isEmpty() || username.isEmpty())
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            else if(email.length < 5 || password.length < 4 || username.length < 2)
                Toast.makeText(context, "Данные слишком короткие", Toast.LENGTH_SHORT).show()
            else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        val db = FirebaseDatabase.getInstance("https://vgroup-48801-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("Users")
                        val id = FirebaseAuth.getInstance().currentUser?.uid

                        if(id != null){
                            val userInfo = Users(id, username, password, getRole(temporarilyPassword), getGroup(temporarilyPassword))

                            db.child(id).setValue(userInfo)
                            startActivity(Intent(context, MainActivity::class.java))
                        }
                        else
                            Toast.makeText(context, "Oh, we catch some error. Try again", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener{
                        Toast.makeText(context, "Print trust data", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.ibSingUpEye.setOnClickListener { togglePasswordVisibility() }
        binding.btSignUpBack.setOnClickListener { finish() }
    }

    fun init(){
        context = this
        binding.etSignUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        setFieldIfNotNull()
    }

    fun getRole(password: String): String{
        return if(password.substring(0, 2) == "&2") "Teacher"
            else if(password == "55%0") "Scheduler"
            else if(password == "331*") "Admin"
            else "Student"
    }

    fun getGroup(password: String): String{
        if(getRole(password) != "Student")
            return ""
        return password.substring(2)
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {

            binding.etSignUpPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.ibSingUpEye.setBackgroundResource(R.drawable.vc_eye_on)
        } else {
            binding.etSignUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.ibSingUpEye.setBackgroundResource(R.drawable.vc_eye_off)
        }

        binding.etSignUpPassword.setSelection(binding.etSignUpPassword.text.length)
    }

    fun setFieldIfNotNull(){
        val mail = intent.getStringExtra("user_mail").toString()
        val password = intent.getStringExtra("user_password").toString()

        if(mail.isEmpty().not())    binding.etSignUpEmail.setText(mail)
        if(password.isEmpty().not())    binding.etSignUpPassword.setText(password)
    }
}