package com.example.fitnesapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback


class LogonActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logon)

        val userName: EditText = findViewById(R.id.name_user)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPassword: EditText = findViewById(R.id.user_password)
        val button: Button = findViewById(R.id.button_reg)
        val TransitionToAuth: TextView = findViewById(R.id.transition_to_auth)

        TransitionToAuth.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }

        fun navigateToMenu() {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        button.setOnClickListener {
            val name = userName.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(name == "" || email == "" || password == "") {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val user = User(name, email, password)

                RetrofitNetwork.instance.registerUser(user).enqueue(object :
                    Callback<RegisterResponse> {
                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.isSuccessful) {
                            Log.d("API_RESPONSE", response.body()?.message?: "No message")
                            navigateToMenu()
                        } else {
                            Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Log.e("API_ERROR", "Network error: ${t.message}")
                    }
                })

                userName.text.clear()
                userEmail.text.clear()
                userPassword.text.clear()
            }
        }

    }
}
