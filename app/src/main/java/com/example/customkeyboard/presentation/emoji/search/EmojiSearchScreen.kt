package com.example.customkeyboard.presentation.emoji.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customkeyboard.presentation.emoji.search.data.EmojiDataProvider
import com.example.customkeyboard.presentation.emoji.search.model.EmojiCategory
import kotlinx.coroutines.delay

@Composable
fun EmojiSearchScreen(
    recentEmojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) {
        EmojiDataProvider.searchEmojis(searchQuery)
    }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Auto-focus on search field when screen opens
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2D2D2D))
    ) {
        // Search Bar
        EmojiSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onClear = { searchQuery = "" },
            onBack = onBack,
            focusRequester = focusRequester,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Results Section
        if (searchQuery.isEmpty()) {
            // Show recent emojis when no search
            RecentEmojisSection(
                recentEmojis = recentEmojis,
                onEmojiSelected = onEmojiSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        } else {
            // Show search results
            if (searchResults.isEmpty()) {
                // No results found
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No emojis found",
                        color = Color(0xFF999999),
                        fontSize = 16.sp
                    )
                }
            } else {
                // Display search results
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(48.dp),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(searchResults) { emojiItem ->
                        EmojiGridItem(
                            emoji = emojiItem.emoji,
                            onClick = { onEmojiSelected(emojiItem.emoji) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmojiSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onBack: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF3D3D3D),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Search Icon
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF999999),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Search Input Field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Search",
                        color = Color(0xFF999999),
                        fontSize = 16.sp
                    )
                }

                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    )
                )
            }

            // Clear Button
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color(0xFF999999)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentEmojisSection(
    recentEmojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Recent Label
        Text(
            text = "Recents",
            color = Color(0xFF999999),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        // Recent Emojis Grid
        if (recentEmojis.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent emojis",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(48.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(recentEmojis.take(20)) { emoji ->
                    EmojiGridItem(
                        emoji = emoji,
                        onClick = { onEmojiSelected(emoji) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmojiGridItem(
    emoji: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clickable(onClick = onClick)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmojiPanelScreen(
    viewModel: EmojiViewModel,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSearchScreen by remember { mutableStateOf(false) }
    val recentEmojis by viewModel.recentEmojis.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        // Main Emoji Panel
        AnimatedVisibility(
            visible = !showSearchScreen,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            MainEmojiPanel(
                recentEmojis = recentEmojis,
                onEmojiSelected = { emoji ->
                    viewModel.addRecentEmoji(emoji)
                    onEmojiSelected(emoji)
                },
                onSearchClick = { showSearchScreen = true }
            )
        }

        // Search Screen
        AnimatedVisibility(
            visible = showSearchScreen,
            enter = slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            EmojiSearchScreen(
                recentEmojis = recentEmojis,
                onEmojiSelected = { emoji ->
                    viewModel.addRecentEmoji(emoji)
                    onEmojiSelected(emoji)
                    showSearchScreen = false
                },
                onBack = { showSearchScreen = false }
            )
        }
    }
}

@Composable
fun MainEmojiPanel(
    recentEmojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(EmojiCategory.SMILEYS_EMOTION) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2D2D2D))
    ) {
        // Top Section with Recent Emojis Row
        TopEmojiRow(
            recentEmojis = recentEmojis,
            onEmojiSelected = onEmojiSelected,
            onSearchClick = onSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        HorizontalDivider(
            color = Color(0xFF3D3D3D),
            thickness = 1.dp
        )

        // Main Emoji Grid
        Box(modifier = Modifier.weight(1f)) {
            val categoryEmojis = remember(selectedCategory) {
                EmojiDataProvider.getEmojisByCategory(selectedCategory)
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(48.dp),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categoryEmojis) { emojiItem ->
                    EmojiGridItem(
                        emoji = emojiItem.emoji,
                        onClick = { onEmojiSelected(emojiItem.emoji) }
                    )
                }
            }
        }

        HorizontalDivider(
            color = Color(0xFF3D3D3D),
            thickness = 1.dp
        )

        // Category Tabs
        EmojiCategoryTabs(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
fun TopEmojiRow(
    recentEmojis: List<String>,
    onEmojiSelected: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Search Button
        Surface(
            onClick = onSearchClick,
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color(0xFF3D3D3D)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search emoji",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Recent Emojis (showing top 5-6)
        recentEmojis.take(6).forEach { emoji ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onEmojiSelected(emoji) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun EmojiCategoryTabs(
    selectedCategory: EmojiCategory,
    onCategorySelected: (EmojiCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0xFF2D2D2D))
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryTab(
            emoji = "😊",
            category = EmojiCategory.SMILEYS_EMOTION,
            isSelected = selectedCategory == EmojiCategory.SMILEYS_EMOTION,
            onClick = { onCategorySelected(EmojiCategory.SMILEYS_EMOTION) }
        )

        CategoryTab(
            emoji = "👋",
            category = EmojiCategory.PEOPLE_BODY,
            isSelected = selectedCategory == EmojiCategory.PEOPLE_BODY,
            onClick = { onCategorySelected(EmojiCategory.PEOPLE_BODY) }
        )

        CategoryTab(
            emoji = "🐶",
            category = EmojiCategory.ANIMALS_NATURE,
            isSelected = selectedCategory == EmojiCategory.ANIMALS_NATURE,
            onClick = { onCategorySelected(EmojiCategory.ANIMALS_NATURE) }
        )

        CategoryTab(
            emoji = "🍕",
            category = EmojiCategory.FOOD_DRINK,
            isSelected = selectedCategory == EmojiCategory.FOOD_DRINK,
            onClick = { onCategorySelected(EmojiCategory.FOOD_DRINK) }
        )

        CategoryTab(
            emoji = "✈️",
            category = EmojiCategory.TRAVEL_PLACES,
            isSelected = selectedCategory == EmojiCategory.TRAVEL_PLACES,
            onClick = { onCategorySelected(EmojiCategory.TRAVEL_PLACES) }
        )

        CategoryTab(
            emoji = "⚽",
            category = EmojiCategory.ACTIVITIES,
            isSelected = selectedCategory == EmojiCategory.ACTIVITIES,
            onClick = { onCategorySelected(EmojiCategory.ACTIVITIES) }
        )

        CategoryTab(
            emoji = "💡",
            category = EmojiCategory.OBJECTS,
            isSelected = selectedCategory == EmojiCategory.OBJECTS,
            onClick = { onCategorySelected(EmojiCategory.OBJECTS) }
        )

        CategoryTab(
            emoji = "❤️",
            category = EmojiCategory.SYMBOLS,
            isSelected = selectedCategory == EmojiCategory.SYMBOLS,
            onClick = { onCategorySelected(EmojiCategory.SYMBOLS) }
        )
    }
}

@Composable
fun CategoryTab(
    emoji: String,
    category: EmojiCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF4D4D4D) else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}


enum class KeyboardMode {
    TEXT,
    EMOJI
}

@Composable
fun YourKeyboardLayout(
    emojiViewModel: EmojiViewModel,
    onEmojiSelected: (String) -> Unit,
    onTextInput: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var keyboardMode by remember { mutableStateOf(KeyboardMode.TEXT) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2D2D2D))
    ) {
        when (keyboardMode) {
            KeyboardMode.TEXT -> {
                // Your existing text keyboard layout
                YourTextKeyboardLayout(
                    onTextInput = onTextInput,
                    onEmojiButtonClick = { keyboardMode = KeyboardMode.EMOJI },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Adjust as needed
                )
            }

            KeyboardMode.EMOJI -> {
                // Emoji panel
                EmojiPanelScreen(
                    viewModel = emojiViewModel,
                    onEmojiSelected = onEmojiSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp) // Adjust as needed
                )

                // Back to text keyboard button
                TextButton(
                    onClick = { keyboardMode = KeyboardMode.TEXT },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFF3D3D3D))
                ) {
                    Text(
                        text = "ABC",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun YourTextKeyboardLayout(
    onTextInput: (String) -> Unit,
    onEmojiButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // This is a placeholder for your existing keyboard layout
    // Replace this with your actual keyboard implementation
    Column(
        modifier = modifier
            .background(Color(0xFF2D2D2D))
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Your Text Keyboard Layout Here",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        // Example: Emoji button in your keyboard
        Button(
            onClick = onEmojiButtonClick,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("😊 Emoji")
        }
    }
}

@Composable
fun YourKeyboardLayout(
    emojiViewModel: EmojiViewModel,
    onEmojiSelected: (String) -> Unit,
    onTextInput: (String) -> Unit
) {
    var showEmojiPanel by remember { mutableStateOf(false) }

    if (showEmojiPanel) {
        EmojiPanelScreen(
            viewModel = emojiViewModel,
            onEmojiSelected = onEmojiSelected,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )

        // Back button
        Button(onClick = { showEmojiPanel = false }) {
            Text("ABC")
        }
    } else {
        YourTextKeyboardLayout(
            onTextInput = onTextInput,
            onEmojiButtonClick = { showEmojiPanel = true }
        )
    }
}