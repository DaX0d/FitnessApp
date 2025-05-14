package com.example.fitnesapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        lifecycleScope.launch {
            // Имитация загрузки (можно удалить в продакшене)
            delay(1000)

            checkAuthState()
        }
    }


    private fun checkAuthState() {
        val token = SecureTokenStorage.TokenStorage.getToken(this)

        val destination = if (!token.isNullOrEmpty()) {
            MenuActivity::class.java
        } else {
            AuthActivity::class.java
        }

        startActivity(Intent(this, destination).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}