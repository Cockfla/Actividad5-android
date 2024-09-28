package com.example.ejemploactividad5

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PokeViewModelFactory(private val repository: PokeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokeViewModel::class.java)) {
            return PokeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}