package com.example.fitnesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.load
import coil.transform.CircleCropTransformation
import com.example.fitnesapp.databinding.ActivityProfileBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        val apiService = Retrofit.Builder()
            .baseUrl("https://your-api-url.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApiService::class.java)

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
}