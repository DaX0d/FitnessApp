package com.example.fitnesapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val context: Context
) : ViewModel() {
    private val _userData = MutableLiveData<UserProfile>()
    val userData: LiveData<UserProfile> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun loadUserData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = profileRepository.getUserProfile()
                if (response.isSuccessful) {
                    _userData.value = response.body()
                } else {
                    _errorMessage.value = "Ошибка загрузки данных"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Нет соединения с сервером"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        SecureTokenStorage.TokenStorage.clearToken(context)
    }
}