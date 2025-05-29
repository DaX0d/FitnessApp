package com.example.fitnesapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val appModule = module {
    single { RetrofitClient.workoutService }
    single { WorkoutRepository(get()) }
    viewModel { WorkoutViewModel(get()) }
}

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}