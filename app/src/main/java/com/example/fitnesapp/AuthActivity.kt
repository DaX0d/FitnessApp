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

        initViews()
        initViewModel()
        setupObservers()
    }

    private fun initViews() {
        val userEmailAuth: EditText = findViewById(R.id.user_email_auth)
        val userPasswordAuth: EditText = findViewById(R.id.user_password_auth)
        val buttonAuth: Button = findViewById(R.id.button_auth)
        val transitionToMain: TextView = findViewById(R.id.transition_to_main)

        transitionToMain.setOnClickListener {
            navigateToMain()
        }

        buttonAuth.setOnClickListener {
            val email = userEmailAuth.text.toString().trim()
            val password = userPasswordAuth.text.toString().trim()

            if (validateInput(email, password)) {
                viewModel.login(email, password)
                clearInputFields(userEmailAuth, userPasswordAuth)
            }
        }
    }

    private fun initViewModel() {
        val apiService = AuthApiService.create("https://your-api-url.com/", this)
        val authRepository = AuthRepository(apiService, this)
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(authRepository)
        )[LoginViewModel::class.java]
    }

    private fun setupObservers() {
        viewModel.loginResult.observe(this) { result ->
            handleLoginResult(result)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() -> {
                showToast("Не все поля заполнены")
                false
            }
            else -> true
        }
    }

    private fun handleLoginResult(result: Result<LoginResponse>) {
        when {
            result.isSuccess -> navigateToMenu()
            result.isFailure -> showError(result.exceptionOrNull()?.message)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, LogonActivity::class.java))
    }

    private fun navigateToMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun clearInputFields(vararg fields: EditText) {
        fields.forEach { it.text.clear() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(
            this,
            "Ошибка входа: ${errorMessage ?: "Неизвестная ошибка"}",
            Toast.LENGTH_SHORT
        ).show()
    }
}