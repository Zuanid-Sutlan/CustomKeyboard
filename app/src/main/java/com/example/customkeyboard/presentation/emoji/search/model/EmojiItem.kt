package com.example.customkeyboard.presentation.emoji.search.model

data class EmojiItem(
    val emoji: String,
    val keywords: List<String>,
    val category: EmojiCategory
)

enum class EmojiCategory {
    SMILEYS_EMOTION,
    PEOPLE_BODY,
    ANIMALS_NATURE,
    FOOD_DRINK,
    TRAVEL_PLACES,
    ACTIVITIES,
    OBJECTS,
    SYMBOLS,
    FLAGS
}