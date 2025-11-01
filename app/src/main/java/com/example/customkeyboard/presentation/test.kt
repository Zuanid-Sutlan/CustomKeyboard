//package com.example.customkeyboard.presentation
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shadow
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.hapticfeedback.HapticFeedbackType
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalHapticFeedback
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.customkeyboard.R
//
//private enum class KeyboardMode {
//    LOWERCASE, UPPERCASE, NUMBERS, SYMBOLS, SIGN_LANGUAGE
//}
//
//@Composable
//fun AccessibleKeyboard(
//    onKeyPress: (String) -> Unit,
//    onDelete: () -> Unit,
//    onSpace: () -> Unit,
//    onEnter: () -> Unit,
//    onCloseKeyboard: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var keyboardMode by remember { mutableStateOf(KeyboardMode.LOWERCASE) }
//    var capsLockEnabled by remember { mutableStateOf(false) }
//    val haptic = LocalHapticFeedback.current
//
//    // Text suggestions for predictive text
//    var suggestions by remember { mutableStateOf(listOf("Hello", "Thanks", "Yes", "No", "Help")) }
//
//    val lowercaseKeys = listOf(
//        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
//        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
//        listOf("z", "x", "c", "v", "b", "n", "m")
//    )
//
//    val uppercaseKeys = listOf(
//        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
//        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
//        listOf("Z", "X", "C", "V", "B", "N", "M")
//    )
//
//    val numberKeys = listOf(
//        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
//        listOf("@", "#", "$", "%", "&", "*", "(", ")", "-", "_"),
//        listOf("+", "=", "/", ":", ";", ",", ".", "?", "!")
//    )
//
//    val symbolKeys = listOf(
//        listOf("~", "`", "|", "•", "√", "π", "÷", "×", "{", "}"),
//        listOf("€", "£", "¥", "¢", "^", "°", "¶", "\\", "[", "]"),
//        listOf("<", ">", "«", "»", "©", "®", "™", "℅", "¿", "¡")
//    )
//
//    // Common sign language phrases/letters
//    val signLanguagePhrases = listOf(
//        "👋 Hello", "👍 Yes", "👎 No", "🙏 Please", "🙏 Thank you",
//        "❓ Question", "👌 OK", "✋ Stop", "👏 Congratulations", "❤️ Love"
//    )
//
//    val currentKeys = when (keyboardMode) {
//        KeyboardMode.LOWERCASE -> lowercaseKeys
//        KeyboardMode.UPPERCASE -> uppercaseKeys
//        KeyboardMode.NUMBERS -> numberKeys
//        KeyboardMode.SYMBOLS -> symbolKeys
//        KeyboardMode.SIGN_LANGUAGE -> emptyList()
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .background(Color(0xFF1E1E2E))
//            .padding(vertical = 8.dp)
//    ) {
//        // Suggestion bar
//        AnimatedVisibility(visible = keyboardMode != KeyboardMode.SIGN_LANGUAGE) {
//            LazyRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp, vertical = 4.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(suggestions) { suggestion ->
//                    SuggestionChip(
//                        text = suggestion,
//                        onClick = {
//                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                            onKeyPress(suggestion)
//                        }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(4.dp))
//
//        // Main keyboard content
//        if (keyboardMode == KeyboardMode.SIGN_LANGUAGE) {
//            SignLanguageKeyboard(
//                phrases = signLanguagePhrases,
//                onPhraseSelect = { phrase ->
//                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                    onKeyPress(phrase)
//                }
//            )
//        } else {
//            // Regular keyboard rows
//            currentKeys.forEachIndexed { rowIndex, keys ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 4.dp, vertical = 3.dp),
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Add shift key for first row of letters
//                    if (rowIndex == 1 && (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE)) {
//                        ShiftKey(
//                            isUppercase = keyboardMode == KeyboardMode.UPPERCASE,
//                            capsLockEnabled = capsLockEnabled,
//                            onClick = {
//                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                if (keyboardMode == KeyboardMode.LOWERCASE) {
//                                    keyboardMode = KeyboardMode.UPPERCASE
//                                } else {
//                                    keyboardMode = KeyboardMode.LOWERCASE
//                                }
//                            },
//                            onLongClick = {
//                                capsLockEnabled = !capsLockEnabled
//                                keyboardMode = if (capsLockEnabled) KeyboardMode.UPPERCASE else KeyboardMode.LOWERCASE
//                            }
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                    } else if (rowIndex == 1) {
//                        Spacer(modifier = Modifier.width(50.dp))
//                    }
//
//                    keys.forEach { key ->
//                        KeyButton(
//                            text = key,
//                            onClick = {
//                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                                onKeyPress(key)
//                                if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
//                                    keyboardMode = KeyboardMode.LOWERCASE
//                                }
//                            }
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                    }
//
//                    // Add delete key for last letter row
//                    if (rowIndex == 2 && (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE)) {
//                        DeleteKey(
//                            onClick = {
//                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                onDelete()
//                            }
//                        )
//                    }
//                }
//            }
//
//            // Bottom row with mode switchers and space
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp, vertical = 3.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // Numbers/Symbols toggle
//                ModeKey(
//                    text = if (keyboardMode == KeyboardMode.NUMBERS || keyboardMode == KeyboardMode.SYMBOLS) "ABC" else "123",
//                    onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        keyboardMode = when (keyboardMode) {
//                            KeyboardMode.NUMBERS, KeyboardMode.SYMBOLS -> KeyboardMode.LOWERCASE
//                            else -> KeyboardMode.NUMBERS
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                // Symbol key (only visible in number mode)
//                if (keyboardMode == KeyboardMode.NUMBERS || keyboardMode == KeyboardMode.SYMBOLS) {
//                    ModeKey(
//                        text = if (keyboardMode == KeyboardMode.SYMBOLS) "123" else "#+=" ,
//                        onClick = {
//                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                            keyboardMode = if (keyboardMode == KeyboardMode.SYMBOLS) {
//                                KeyboardMode.NUMBERS
//                            } else {
//                                KeyboardMode.SYMBOLS
//                            }
//                        }
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                }
//
//                // Sign language mode
//                IconModeKey(
//                    icon = Icons.Default.SignLanguage,
//                    onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        keyboardMode = if (keyboardMode == KeyboardMode.SIGN_LANGUAGE) {
//                            KeyboardMode.LOWERCASE
//                        } else {
//                            KeyboardMode.SIGN_LANGUAGE
//                        }
//                    },
//                    isActive = keyboardMode == KeyboardMode.SIGN_LANGUAGE
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                // Comma (only in letter mode)
//                if (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE) {
//                    KeyButton(
//                        text = ",",
//                        onClick = {
//                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                            onKeyPress(",")
//                        }
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                }
//
//                // Space bar
//                SpaceKey(
//                    onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        onSpace()
//                    }
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                // Period (only in letter mode)
//                if (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE) {
//                    KeyButton(
//                        text = ".",
//                        onClick = {
//                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                            onKeyPress(".")
//                        }
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                }
//
//                // Enter key
//                EnterKey(
//                    onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        onEnter()
//                    }
//                )
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                // Close keyboard
//                IconModeKey(
//                    icon = Icons.Default.KeyboardHide,
//                    onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        onCloseKeyboard()
//                    }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun KeyButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .size(38.dp, 48.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF4A4A6A) else Color(0xFF2A2A3E),
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Image(painter = painterResource(id = R.drawable.a), contentDescription = "a", modifier = Modifier.fillMaxSize())
//        Box(modifier.fillMaxSize().background(Color(0xFF2A2A3E).copy(alpha = 0.6f)))
//        Text(
//            text = text,
//            color = Color.Blue,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Medium,
//            style = TextStyle(
//                shadow = Shadow(
//                    color = Color.Blue, offset = Offset(1.0f, 3.0f), blurRadius = 6f
//                )
//            )
//        )
//    }
//}
//
//@Composable
//private fun ShiftKey(
//    isUppercase: Boolean,
//    capsLockEnabled: Boolean,
//    onClick: () -> Unit,
//    onLongClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .width(50.dp)
//            .height(48.dp)
//            .scale(scale)
//            .background(
//                color = when {
//                    capsLockEnabled -> Color(0xFF6B4A9E)
//                    isUppercase -> Color(0xFF4A4A6A)
//                    else -> Color(0xFF2A2A3E)
//                },
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() },
//                    onLongPress = { onLongClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.KeyboardArrowUp,
//            contentDescription = "Shift",
//            tint = Color.White,
//            modifier = Modifier.size(28.dp)
//        )
//    }
//}
//
//@Composable
//fun DeleteKey(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .width(50.dp)
//            .height(48.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF6B4A4A) else Color(0xFF3E2A2A),
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.Backspace,
//            contentDescription = "Delete",
//            tint = Color.White,
//            modifier = Modifier.size(24.dp)
//        )
//    }
//}
//
//@Composable
//fun SpaceKey(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
////            .weight(1f)
//            .height(48.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF4A4A6A) else Color(0xFF2A2A3E),
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Space",
//            color = Color.White,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//fun EnterKey(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .width(60.dp)
//            .height(48.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF4A6B4A) else Color(0xFF2A3E2A),
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.KeyboardReturn,
//            contentDescription = "Enter",
//            tint = Color.White,
//            modifier = Modifier.size(24.dp)
//        )
//    }
//}
//
//@Composable
//fun ModeKey(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .width(50.dp)
//            .height(48.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF4A4A6A) else Color(0xFF3A3A4E),
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = Color.White,
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//@Composable
//fun IconModeKey(
//    icon: ImageVector,
//    onClick: () -> Unit,
//    isActive: Boolean = false,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .size(48.dp)
//            .scale(scale)
//            .background(
//                color = when {
//                    isActive -> Color(0xFF6B4A9E)
//                    isPressed -> Color(0xFF4A4A6A)
//                    else -> Color(0xFF3A3A4E)
//                },
//                shape = RoundedCornerShape(8.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = Color.White,
//            modifier = Modifier.size(24.dp)
//        )
//    }
//}
//
//@Composable
//fun SuggestionChip(
//    text: String,
//    onClick: () -> Unit
//) {
//    Surface(
//        modifier = Modifier.clickable { onClick() },
//        shape = RoundedCornerShape(16.dp),
//        color = Color(0xFF3A3A4E)
//    ) {
//        Text(
//            text = text,
//            color = Color.White,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//        )
//    }
//}
//
//@Composable
//fun SignLanguageKeyboard(
//    phrases: List<String>,
//    onPhraseSelect: (String) -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Text(
//            text = "Sign Language Quick Phrases",
//            color = Color.White,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        )
//
//        phrases.chunked(2).forEach { rowPhrases ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                rowPhrases.forEach { phrase ->
//                    SignLanguageButton(
//                        text = phrase,
//                        onClick = { onPhraseSelect(phrase) },
//                        modifier = Modifier.weight(1f)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SignLanguageButton(
//    text: String,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var isPressed by remember { mutableStateOf(false) }
//    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")
//
//    Box(
//        modifier = modifier
//            .height(64.dp)
//            .scale(scale)
//            .background(
//                color = if (isPressed) Color(0xFF4A6B9E) else Color(0xFF2A4A6E),
//                shape = RoundedCornerShape(12.dp)
//            )
//            .pointerInput(Unit) {
//                detectTapGestures(
//                    onPress = {
//                        isPressed = true
//                        tryAwaitRelease()
//                        isPressed = false
//                    },
//                    onTap = { onClick() }
//                )
//            }
//            .padding(12.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = Color.White,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Medium,
//            textAlign = TextAlign.Center
//        )
//    }
//}