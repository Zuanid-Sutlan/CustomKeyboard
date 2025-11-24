package com.example.customkeyboard.presentation.emoji.search.helper

// EmojiViewModelFactory.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.customkeyboard.presentation.emoji.search.EmojiViewModel
import com.example.customkeyboard.presentation.emoji.search.utils.EmojiPreferences

class EmojiViewModelFactory(
    private val emojiPreferences: EmojiPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmojiViewModel::class.java)) {
            return EmojiViewModel(emojiPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}