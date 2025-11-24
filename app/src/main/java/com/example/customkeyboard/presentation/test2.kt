/*
package com.example.customkeyboard.presentation


import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class KeyboardMode {
    LOWERCASE, UPPERCASE, NUMBERS, SYMBOLS
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun KeyboardPreview(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    CustomKeyboardTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ModernKeyboard(
                onKeyPress = { key -> text += key },
                onDelete = { if (text.isNotEmpty()) text = text.dropLast(1) },
                onSpace = { text += " " },
                onEnter = { text += "\n" },
                onCloseKeyboard = {  },
                onSwitchKeyboard = {
                    // Open system keyboard picker
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showInputMethodPicker()
                }
            )
        }
    }
}

@Composable
fun ModernKeyboard(
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onCloseKeyboard: () -> Unit,
    onSwitchKeyboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var keyboardMode by remember { mutableStateOf(KeyboardMode.UPPERCASE) }
    var capsLockEnabled by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val lowercaseKeys = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("z", "x", "c", "v", "b", "n", "m")
    )

    val uppercaseKeys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    val numberKeys = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("@", "#", "$", "_", "&", "-", "+", "(", ")", "/"),
        listOf("*", "\"", "'", ":", ";", "!", "?", ",", ".")
    )

    val symbolKeys = listOf(
        listOf("[", "]", "{", "}", "#", "%", "^", "*", "+", "="),
        listOf("_", "\\", "|", "~", "<", ">", "€", "£", "¥", "•"),
        listOf(".", ",", "?", "!", "'", "\"", ";", ":", "/", "-")
    )

    val currentKeys = when (keyboardMode) {
        KeyboardMode.LOWERCASE -> lowercaseKeys
        KeyboardMode.UPPERCASE -> uppercaseKeys
        KeyboardMode.NUMBERS -> numberKeys
        KeyboardMode.SYMBOLS -> symbolKeys
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2B2B))
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        // First row - QWERTY
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            currentKeys[0].forEach { key ->
                KeyButton(
                    text = key,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(key)
                        if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                            keyboardMode = KeyboardMode.LOWERCASE
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Second row - ASDF with padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Spacer(modifier = Modifier.width(20.dp))

            currentKeys[1].forEach { key ->
                KeyButton(
                    text = key,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(key)
                        if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                            keyboardMode = KeyboardMode.LOWERCASE
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
        }

        // Third row - ZXCV with Shift and Delete
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shift key
            if (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE) {
                ShiftKey(
                    isUppercase = keyboardMode == KeyboardMode.UPPERCASE,
                    capsLockEnabled = capsLockEnabled,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (keyboardMode == KeyboardMode.LOWERCASE) {
                            keyboardMode = KeyboardMode.UPPERCASE
                        } else {
                            keyboardMode = KeyboardMode.LOWERCASE
                        }
                    },
                    onLongClick = {
                        capsLockEnabled = !capsLockEnabled
                        keyboardMode =
                            if (capsLockEnabled) KeyboardMode.UPPERCASE else KeyboardMode.LOWERCASE
                    },
                    modifier = Modifier.weight(1.3f)
                )
            } else {
                SymbolShiftKey(
                    isSymbols = keyboardMode == KeyboardMode.SYMBOLS,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        keyboardMode = if (keyboardMode == KeyboardMode.SYMBOLS) {
                            KeyboardMode.NUMBERS
                        } else {
                            KeyboardMode.SYMBOLS
                        }
                    },
                    modifier = Modifier.weight(1.3f)
                )
            }

            currentKeys[2].forEach { key ->
                KeyButton(
                    text = key,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(key)
                        if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                            keyboardMode = KeyboardMode.LOWERCASE
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Delete key
            DeleteKey(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDelete()
                },
//                onLongClick = {
//                    while (true) {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        onDelete()
//                    }
//                },
                modifier = Modifier.weight(1.3f)
            )
        }

        // Bottom row - Mode switchers and space
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Numbers/ABC toggle
            ModeKey(
                text = if (keyboardMode == KeyboardMode.NUMBERS || keyboardMode == KeyboardMode.SYMBOLS) "ABC" else "?123",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    keyboardMode = when (keyboardMode) {
                        KeyboardMode.NUMBERS, KeyboardMode.SYMBOLS -> KeyboardMode.LOWERCASE
                        else -> KeyboardMode.NUMBERS
                    }
                },
                modifier = Modifier.weight(1.5f)
            )

            // Emoji button
            EmojiKey(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    // Handle emoji keyboard
                },
                modifier = Modifier.weight(1f)
            )

            // Globe/Language button
            GlobeKey(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    // Handle language switch
                },
                modifier = Modifier.weight(1f)
            )

            // Space bar
            SpaceKey(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSpace()
                },
                modifier = Modifier.weight(4f)
            )

            // Period
            KeyButton(
                text = ".",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onKeyPress(".")
                },
                modifier = Modifier.weight(1f)
            )

            // Enter key
            EnterKey(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onEnter()
                },
                modifier = Modifier.weight(1.5f)
            )
        }

//        BottomRow(
//            onCloseKeyboard = {
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                onCloseKeyboard()
//            },
//            onSwitchKeyboard = {
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                onSwitchKeyboard()
//            }
//        )
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF3A3A3A)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ShiftKey(
    isUppercase: Boolean,
    capsLockEnabled: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = when {
                    isPressed -> Color(0xFF505050)
                    capsLockEnabled || isUppercase -> Color(0xFF4A4A4A)
                    else -> Color(0xFF3A3A3A)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Shift",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun SymbolShiftKey(
    isSymbols: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isSymbols) "123" else "=\\<",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

*/
/*@Composable
fun DeleteKey(
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        onLongClick()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Backspace,
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}*/
/*

@Composable
fun DeleteKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        val startTime = System.currentTimeMillis()
                        var job: Job? = null

                        // Initial single delete
                        onClick()

                        // Wait for long press threshold (500ms)
                        delay(500)

                        // Start continuous deletion
                        job = CoroutineScope(Dispatchers.IO).launch {
                            while (isPressed) {
                                onClick()
                                delay(50) // Delete every 50ms
                            }
                        }

                        tryAwaitRelease()
                        isPressed = false
                        job.cancel()
                    },
                    onTap = {
                        // Single tap handled in onPress
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Backspace,
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SpaceKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EN · FR",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun EnterKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF6DDCC3) else Color(0xFF5FCAAF),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardReturn,
            contentDescription = "Enter",
            tint = Color(0xFF1A1A1A),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ModeKey(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF6DDCC3) else Color(0xFF5FCAAF),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF1A1A1A),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmojiKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "😊",
            fontSize = 22.sp
        )
    }
}

@Composable
fun GlobeKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}


// Bottom control row - Hide keyboard and Switch keyboard
@Composable
fun BottomRow(modifier: Modifier = Modifier, onSwitchKeyboard: () -> Unit, onCloseKeyboard: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hide/Close keyboard button (left)
        ControlKey(
            icon = Icons.Default.KeyboardArrowDown,
            onClick = onCloseKeyboard,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.weight(6f))

        // Switch keyboard button (right)
        ControlKey(
            icon = Icons.Default.Keyboard,
            onClick = onSwitchKeyboard,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun ControlKey(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(44.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF404040) else Color(0xFF2B2B2B),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp)
        )
    }
}*/


package com.example.customkeyboard.presentation


import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.customkeyboard.R
import com.example.customkeyboard.presentation.emoji.EmojiPickerComposable

enum class KeyboardMode {
    LOWERCASE, UPPERCASE, NUMBERS, SYMBOLS
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun KeyboardPreview(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    CustomKeyboardTheme(darkTheme = true) {
        var text by remember { mutableStateOf("") }

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            ModernKeyboard(
                onKeyPress = { key -> text += key },
                onDelete = { if (text.isNotEmpty()) text = text.dropLast(1) },
                onSpace = { text += " " },
                onEnter = { text += "\n" },
                onCloseKeyboard = {  },
                onSwitchKeyboard = {
                    // Open system keyboard picker
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showInputMethodPicker()
                }
            )
        }
    }
}

@Composable
fun ModernKeyboard(
    onKeyPress: (String) -> Unit,
    onDelete: () -> Unit,
    onSpace: () -> Unit,
    onEnter: () -> Unit,
    onCloseKeyboard: () -> Unit,
    onSwitchKeyboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var keyboardMode by remember { mutableStateOf(KeyboardMode.UPPERCASE) }
    var capsLockEnabled by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val lowercaseKeys = listOf(
        listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p"),
        listOf("a", "s", "d", "f", "g", "h", "j", "k", "l"),
        listOf("z", "x", "c", "v", "b", "n", "m")
    )

    val uppercaseKeys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    val numberKeys = listOf(
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        listOf("@", "#", "$", "_", "&", "-", "+", "(", ")", "/"),
        listOf("*", "\"", "'", ":", ";", "!", "?", ",", ".")
    )

    val symbolKeys = listOf(
        listOf("[", "]", "{", "}", "#", "%", "^", "*", "+", "="),
        listOf("_", "\\", "|", "~", "<", ">", "€", "£", "¥", "•"),
        listOf(".", ",", "?", "!", "'", "\"", ";", ":", "/", "-")
    )

    val currentKeys = when (keyboardMode) {
        KeyboardMode.LOWERCASE -> lowercaseKeys
        KeyboardMode.UPPERCASE -> uppercaseKeys
        KeyboardMode.NUMBERS -> numberKeys
        KeyboardMode.SYMBOLS -> symbolKeys
    }
    var showEmojiPicker by remember { mutableStateOf(false) }

    if (showEmojiPicker) {
        Column() {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { showEmojiPicker = false}
                ) { Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null) }
            }
            EmojiPickerComposable(
                onEmojiSelected = { emoji ->
//                        val currentText = textFieldValue.text
//                        val cursorPosition = textFieldValue.selection.start
//                        val newText = currentText.substring(0, cursorPosition) +
//                                emoji +
//                                currentText.substring(cursorPosition)
//                        textFieldValue = TextFieldValue(
//                            text = newText,
//                            selection = TextRange(cursorPosition + emoji.length)
//                        )
                    onKeyPress(emoji)
                },
                modifier = Modifier.height(280.dp)
            )
        }
    } else{
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color(0xFF2B2B2B))
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            // First row - QWERTY
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                currentKeys[0].forEach { key ->
                    KeyButton(
                        text = key,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onKeyPress(key)
                            if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                                keyboardMode = KeyboardMode.LOWERCASE
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Second row - ASDF with padding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                currentKeys[1].forEach { key ->
                    KeyButton(
                        text = key,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onKeyPress(key)
                            if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                                keyboardMode = KeyboardMode.LOWERCASE
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
            }

            // Third row - ZXCV with Shift and Delete
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shift key
                if (keyboardMode == KeyboardMode.LOWERCASE || keyboardMode == KeyboardMode.UPPERCASE) {
                    ShiftKey(
                        isUppercase = keyboardMode == KeyboardMode.UPPERCASE,
                        capsLockEnabled = capsLockEnabled,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            keyboardMode = if (keyboardMode == KeyboardMode.LOWERCASE) {
                                KeyboardMode.UPPERCASE
                            } else {
                                KeyboardMode.LOWERCASE
                            }
                        },
                        onLongClick = {
                            capsLockEnabled = !capsLockEnabled
                            keyboardMode =
                                if (capsLockEnabled) KeyboardMode.UPPERCASE else KeyboardMode.LOWERCASE
                        },
                        modifier = Modifier.weight(1.3f)
                    )
                } else {
                    SymbolShiftKey(
                        isSymbols = keyboardMode == KeyboardMode.SYMBOLS,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            keyboardMode = if (keyboardMode == KeyboardMode.SYMBOLS) {
                                KeyboardMode.NUMBERS
                            } else {
                                KeyboardMode.SYMBOLS
                            }
                        },
                        modifier = Modifier.weight(1.3f)
                    )
                }

                currentKeys[2].forEach { key ->
                    KeyButton(
                        text = key,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onKeyPress(key)
                            if (keyboardMode == KeyboardMode.UPPERCASE && !capsLockEnabled) {
                                keyboardMode = KeyboardMode.LOWERCASE
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Delete key
                DeleteKey(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDelete()
                    },
                    modifier = Modifier.weight(1.3f)
                )
            }

            // Bottom row - Mode switchers and space
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Numbers/ABC toggle
                ModeKey(
                    text = if (keyboardMode == KeyboardMode.NUMBERS || keyboardMode == KeyboardMode.SYMBOLS) "ABC" else "?123",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        keyboardMode = when (keyboardMode) {
                            KeyboardMode.NUMBERS, KeyboardMode.SYMBOLS -> KeyboardMode.LOWERCASE
                            else -> KeyboardMode.NUMBERS
                        }
                    },
                    modifier = Modifier.weight(1.5f)
                )

                // Emoji button
                EmojiKey(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        // Handle emoji keyboard
                        showEmojiPicker = true
                    },
                    modifier = Modifier.weight(1f)
                )

                // Globe/Language button
                GlobeKey(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        // Handle language switch
                    },
                    modifier = Modifier.weight(1f)
                )

                // Space bar
                SpaceKey(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSpace()
                    },
                    modifier = Modifier.weight(4f)
                )

                // Period
                KeyButton(
                    text = ".",
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onKeyPress(".")
                    },
                    modifier = Modifier.weight(1f)
                )

                // Enter key
                EnterKey(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEnter()
                    },
                    modifier = Modifier.weight(1.5f)
                )
            }

//        BottomRow(
//            onCloseKeyboard = {
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                onCloseKeyboard()
//            },
//            onSwitchKeyboard = {
//                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                onSwitchKeyboard()
//            }
//        )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF3A3A3A)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
//        contentAlignment = Alignment.Center,
    ) {
        if (text == "a" || text == "A"){
            Image(
                modifier = Modifier.padding(top = 4.dp, end = 0.dp).size(24.dp).align(Alignment.TopEnd),
                painter = painterResource(R.drawable.a),
                contentDescription = "a"
            )
            Text(
                modifier = Modifier.padding(4.dp).align(Alignment.BottomStart),
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun ShiftKey(
    isUppercase: Boolean,
    capsLockEnabled: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = when {
                    isPressed -> Color(0xFF505050)
                    capsLockEnabled || isUppercase -> Color(0xFF4A4A4A)
                    else -> Color(0xFF3A3A3A)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() },
                    onLongPress = { onLongClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "Shift",
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun SymbolShiftKey(
    isSymbols: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isSymbols) "123" else "=\\<",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

// -------------------------------------------------
// START: CORRECTED DeleteKey
// -------------------------------------------------
@Composable
fun DeleteKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    // 1. Get a CoroutineScope bound to the main thread
    val scope = rememberCoroutineScope()

    // 2. Store the job in a 'remember'ed mutable state
    var job: Job? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true

                        // 3. Perform the initial delete immediately on press
                        onClick()

                        // 4. Launch the continuous delete job on the correct scope
                        job = scope.launch {
                            // Wait for the long-press threshold
                            delay(500)

                            // Start continuous deletion
                            while (true) {
                                onClick()
                                delay(50) // Delete every 50ms
                            }
                        }

                        // 5. Wait for the user to lift their finger
                        tryAwaitRelease()

                        // 6. Cancel the job and reset the press state
                        job?.cancel()
                        isPressed = false
                    },
                    // We don't need onTap, as onPress handles the initial click
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Backspace,
            contentDescription = "Delete",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
// -------------------------------------------------
// END: CORRECTED DeleteKey
// -------------------------------------------------

@Composable
fun SpaceKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EN · FR",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun EnterKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF6DDCC3) else Color(0xFF5FCAAF),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardReturn,
            contentDescription = "Enter",
            tint = Color(0xFF1A1A1A),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ModeKey(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF6DDCC3) else Color(0xFF5FCAAF),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF1A1A1A),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmojiKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "😊",
            fontSize = 22.sp
        )
    }
}

@Composable
fun GlobeKey(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(48.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF505050) else Color(0xFF3A3A3A),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}


// Bottom control row - Hide keyboard and Switch keyboard
@Composable
fun BottomRow(modifier: Modifier = Modifier, onSwitchKeyboard: () -> Unit, onCloseKeyboard: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hide/Close keyboard button (left)
        ControlKey(
            icon = Icons.Default.KeyboardArrowDown,
            onClick = onCloseKeyboard,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.weight(6f))

        // Switch keyboard button (right)
        ControlKey(
            icon = Icons.Default.Keyboard,
            onClick = onSwitchKeyboard,
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun ControlKey(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Box(
        modifier = modifier
            .height(44.dp)
            .scale(scale)
            .background(
                color = if (isPressed) Color(0xFF404040) else Color(0xFF2B2B2B),
                shape = RoundedCornerShape(8.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.size(22.dp)
        )
    }
}