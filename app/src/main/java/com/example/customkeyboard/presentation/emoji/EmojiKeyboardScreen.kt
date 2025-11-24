package com.example.customkeyboard.presentation.emoji

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem

@Composable
fun EmojiKeyboardScreen(modifier: Modifier) {
//    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var textFieldValue by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Text Display Area
//        TextDisplayArea(
//            textFieldValue = textFieldValue,
//            onValueChange = { textFieldValue = it },
//            modifier = Modifier.weight(1f)
//        )
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        // Emoji Picker
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
                        textFieldValue += emoji
                    },
                    modifier = Modifier.height(280.dp)
                )
            }
        }

        // Keyboard
        if (!showEmojiPicker) {
            CustomKeyboard(
                onKeyPress = { key ->
//                    val currentText = textFieldValue.text
//                    val cursorPosition = textFieldValue.selection.start
//                    val newText = currentText.substring(0, cursorPosition) +
//                            key +
//                            currentText.substring(cursorPosition)
//                    textFieldValue = TextFieldValue(
//                        text = newText,
//                        selection = TextRange(cursorPosition + key.length)
//                    )
                    textFieldValue += key
                },
                onBackspace = {
//                    val currentText = textFieldValue.text
//                    val cursorPosition = textFieldValue.selection.start
//                    if (cursorPosition > 0) {
//                        val newText = currentText.substring(0, cursorPosition - 1) +
//                                currentText.substring(cursorPosition)
//                        textFieldValue = TextFieldValue(
//                            text = newText,
//                            selection = TextRange(cursorPosition - 1)
//                        )
//                    }
                    if (textFieldValue.isNotEmpty()) {
                        textFieldValue = textFieldValue.substring(0, textFieldValue.length - 1)
                    }
                },
                onEmojiToggle = { showEmojiPicker = !showEmojiPicker },
                showingEmoji = showEmojiPicker
            )
        }
    }
}

@Composable
fun TextDisplayArea(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            placeholder = { Text("Type here...") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )
    }
}

@Composable
fun EmojiPickerComposable(
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            EmojiPickerView(context).apply {
                setOnEmojiPickedListener { emojiViewItem ->
                    when (emojiViewItem) {
                        else -> {
                            onEmojiSelected(emojiViewItem.emoji)
                        }
                    }
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    )
}

@Composable
fun CustomKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspace: () -> Unit,
    onEmojiToggle: () -> Unit,
    showingEmoji: Boolean
) {
    val keyboardRows = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD3D3D3))
            .padding(4.dp)
    ) {
        // First Row
        KeyboardRow(
            keys = keyboardRows[0],
            onKeyPress = onKeyPress
        )

        // Second Row with padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            keyboardRows[1].forEach { key ->
                KeyButton(
                    text = key,
                    onClick = { onKeyPress(key) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Third Row with Shift and Backspace
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyButton(
                text = "⇧",
                onClick = { },
                modifier = Modifier.weight(1.5f),
                fontSize = 20.sp
            )

            keyboardRows[2].forEach { key ->
                KeyButton(
                    text = key,
                    onClick = { onKeyPress(key) },
                    modifier = Modifier.weight(1f)
                )
            }

            KeyButton(
                text = "⌫",
                onClick = onBackspace,
                modifier = Modifier.weight(1.5f),
                fontSize = 20.sp
            )
        }

        // Fourth Row with Emoji, Space, Enter
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyButton(
                text = "😊",
                onClick = onEmojiToggle,
                modifier = Modifier.weight(1.5f),
                fontSize = 18.sp,
                backgroundColor = if (showingEmoji) Color(0xFFBDBDBD) else Color.White
            )

            KeyButton(
                text = "Space",
                onClick = { onKeyPress(" ") },
                modifier = Modifier.weight(5f),
                fontSize = 14.sp
            )

            KeyButton(
                text = "↵",
                onClick = { onKeyPress("\n") },
                modifier = Modifier.weight(1.5f),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyPress: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        keys.forEach { key ->
            KeyButton(
                text = key,
                onClick = { onKeyPress(key) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    backgroundColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .padding(2.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = Color.Black
        )
    }
}