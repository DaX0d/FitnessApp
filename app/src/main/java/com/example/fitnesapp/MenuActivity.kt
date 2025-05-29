package com.example.fitnesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val button_lk: Button = findViewById(R.id.button)
        val button_tren: Button = findViewById(R.id.button2)

        button_lk.setOnClickListener{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
        }

        button_tren.setOnClickListener{
            val intent = Intent(this, DifficultySelectionActivity::class.java)
            startActivity(intent)
        }
    }
}
