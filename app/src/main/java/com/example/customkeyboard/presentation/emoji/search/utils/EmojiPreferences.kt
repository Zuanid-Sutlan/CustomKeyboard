package com.example.customkeyboard.presentation.emoji.search.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EmojiPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    companion object {
        private const val PREFS_NAME = "emoji_preferences"
        private const val KEY_RECENT_EMOJIS = "recent_emojis"
        private const val MAX_RECENT_EMOJIS = 50
        private const val EMOJI_SEPARATOR = "|"
    }

    suspend fun getRecentEmojis(): List<String> = withContext(Dispatchers.IO) {
        val emojisString = sharedPreferences.getString(KEY_RECENT_EMOJIS, "") ?: ""
        if (emojisString.isEmpty()) {
            emptyList()
        } else {
            emojisString.split(EMOJI_SEPARATOR).filter { it.isNotEmpty() }
        }
    }

    suspend fun addRecentEmoji(emoji: String) = withContext(Dispatchers.IO) {
        val currentEmojis = getRecentEmojis().toMutableList()

        // Remove if already exists (to move it to front)
        currentEmojis.remove(emoji)

        // Add to front
        currentEmojis.add(0, emoji)

        // Keep only max recent emojis
        val trimmedEmojis = currentEmojis.take(MAX_RECENT_EMOJIS)

        // Save
        val emojisString = trimmedEmojis.joinToString(EMOJI_SEPARATOR)
        sharedPreferences.edit()
            .putString(KEY_RECENT_EMOJIS, emojisString)
            .apply()
    }

    suspend fun clearRecentEmojis() = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .remove(KEY_RECENT_EMOJIS)
            .apply()
    }
}