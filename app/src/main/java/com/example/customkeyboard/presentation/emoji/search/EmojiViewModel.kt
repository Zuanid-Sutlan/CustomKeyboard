package com.example.customkeyboard.presentation.emoji.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customkeyboard.presentation.emoji.search.utils.EmojiPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmojiViewModel(
    private val emojiPreferences: EmojiPreferences
) : ViewModel() {

    private val _recentEmojis = MutableStateFlow<List<String>>(emptyList())
    val recentEmojis: StateFlow<List<String>> = _recentEmojis.asStateFlow()

    init {
        loadRecentEmojis()
    }

    private fun loadRecentEmojis() {
        viewModelScope.launch {
            _recentEmojis.value = emojiPreferences.getRecentEmojis()
        }
    }

    fun addRecentEmoji(emoji: String) {
        viewModelScope.launch {
            emojiPreferences.addRecentEmoji(emoji)
            _recentEmojis.value = emojiPreferences.getRecentEmojis()
        }
    }

    fun clearRecentEmojis() {
        viewModelScope.launch {
            emojiPreferences.clearRecentEmojis()
            _recentEmojis.value = emptyList()
        }
    }
}