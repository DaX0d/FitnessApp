package com.example.fitnesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fitnesapp.databinding.ActivityProfileBinding
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        val retrofit = provideRetrofit(applicationContext)
        val apiService = retrofit.create(ProfileApiService::class.java)


        ProfileViewModelFactory(
            profileRepository = ProfileRepository(apiService),
            context = applicationContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        viewModel.loadUserData()

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        val button: Button = findViewById(R.id.btnMenu)

        button.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.userData.observe(this) { user ->
            user?.let {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
                binding.ivAvatar.load(it.avatarUrl) {
                    transformations(CircleCropTransformation())
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun provideRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // ⬅️ вот он
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.12:8000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}