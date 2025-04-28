package com.example.fitnesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class AuthActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)


        val userEmailAuth: EditText = findViewById(R.id.user_email_auth)
        val userPasswordAuth: EditText = findViewById(R.id.user_password_auth)
        val buttonAuth: Button = findViewById(R.id.button_auth)
        val TransitionToMain: TextView = findViewById(R.id.transition_to_main)
        val apiService = AuthApiService.create("https://your-api-url.com/")
        val authRepository = AuthRepository(apiService)
        val sharedPreferences = getSharedPreferences("com.example.fitnesapp", MODE_PRIVATE)

        viewModel = ViewModelProvider(this, LoginViewModelFactory(
            authRepository,
            sharedPreferences
        ))[LoginViewModel::class.java]

        TransitionToMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        setupObservers()

        buttonAuth.setOnClickListener {
            val email = userEmailAuth.text.toString().trim()
            val password = userPasswordAuth.text.toString().trim()

            if(email == "" || password == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                viewModel.login(email, password)

                userEmailAuth.text.clear()
                userPasswordAuth.text.clear()
            }
        }
    }
    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrNull()!!
                    viewModel.saveAuthToken(response.token)
                    startActivity(Intent(this, MenuActivity::class.java))
                    finish()
                }
                result.isFailure -> {
                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
                    Toast.makeText(this, "Login failed: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
