package com.example.customkeyboard.presentation

import android.content.Context
import android.view.inputmethod.InputConnection
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme

class ComposeHexadecimalKeyBoardView(context: Context, private val onKeyPress: (String) -> Unit) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        CustomKeyboardTheme {
            // Pass a lambda to handle key presses.
            KeyboardLayout(onKeyPress = { key ->
                onKeyPress(key)
            })
//            AccessibleKeyboard(
//                onKeyPress = { key ->
//                    onKeyPress(key)
//                },
//                onDelete = {
//                    onKeyPress("DEL")
//                },
//                onEnter = {
//                    onKeyPress("ENTER")
//                },
//                onSpace = {
//                    onKeyPress("SPACE")
//                },
//                onCloseKeyboard = {
//                    onKeyPress("CLOSE")
//                }
//            )
        }
    }

    /**
     * Handles the logic for what to do when a key is pressed.
     */
/*
    private fun handleKeyPress(key: String) {
        val ic = currentInputConnection ?: return

        when (key) {
            "DEL" -> {
                // Delete one character before the cursor
                ic.deleteSurroundingText(1, 0)
            }
            "SPACE" -> {
                // Commit a space
                ic.commitText(" ", 1)
            }
            "ENTER" -> {
                // Commit a newline
                ic.commitText("\n", 1)
            }
            else -> {
                // Commit the character
                ic.commitText(key, 1)
            }
        }
    }
*/
}