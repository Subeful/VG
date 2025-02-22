package com.andrey.vg.screens.entrance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.andrey.vg.R
import com.andrey.vg.databinding.ActivityLoginBinding
import com.andrey.vg.screens.MainActivity
import com.andrey.vg.utils.Cipher
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var context: Context

    var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this
        binding.etLoginPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        binding.btLoginLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val temporarilyPassword = binding.etLoginPassword.text.toString()
            val password = Cipher.encryptPassword(context, temporarilyPassword)

            if (email.isEmpty() || password.isEmpty())
                Toast.makeText(context, "Заполните все данные", Toast.LENGTH_SHORT).show()
            else if (password.length < 4)
                Toast.makeText(context, "Слишком короткий пароль", Toast.LENGTH_SHORT).show()
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                            startActivity(Intent(context, MainActivity::class.java))
                        else
                            Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.lnLoginSignUp.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            intent.putExtra("user_mail", binding.etLoginEmail.text.toString())
            intent.putExtra("user_password", binding.etLoginPassword.text.toString())
            startActivity(intent)
        }
        binding.ibLoginEye.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.etLoginPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.ibLoginEye.setBackgroundResource(R.drawable.vc_eye_on)
        } else {
            binding.etLoginPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.ibLoginEye.setBackgroundResource(R.drawable.vc_eye_off)
        }

        binding.etLoginPassword.setSelection(binding.etLoginPassword.text.length)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}