package com.example.fitnesapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureTokenStorage {
    object TokenStorage {
        private const val PREFS_NAME = "auth_prefs"
        private const val TOKEN_KEY = "jwt_token"

        fun saveToken(context: Context, token: String) {
            getEncryptedPrefs(context).edit {
                putString(TOKEN_KEY, token)
                apply()
            }
        }

        fun getToken(context: Context): String? {
            return getEncryptedPrefs(context).getString(TOKEN_KEY, null)
        }

        // Новый метод для очистки токена
        fun clearToken(context: Context) {
            getEncryptedPrefs(context).edit {
                remove(TOKEN_KEY)
                apply()
            }
        }

        private fun getEncryptedPrefs(context: Context): SharedPreferences {
            return EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}