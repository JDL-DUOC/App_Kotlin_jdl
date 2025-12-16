package com.example.creativeblock.di

import com.example.creativeblock.data.IdeaRepository
import com.example.creativeblock.viewmodel.IdeaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { IdeaRepository(get(), get()) }
    viewModel { IdeaViewModel(get()) }
}