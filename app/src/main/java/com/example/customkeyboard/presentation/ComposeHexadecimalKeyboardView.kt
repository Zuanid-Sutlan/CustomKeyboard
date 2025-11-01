package com.example.customkeyboard.presentation

import android.content.Context
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme

class ComposeHexadecimalKeyBoardView(context: Context, private val onKeyPress: (String) -> Unit) :
    AbstractComposeView(context) {
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {

        val keyboardController = LocalSoftwareKeyboardController.current
        val isKeyboardVisible = WindowInsets.isImeVisible


        CustomKeyboardTheme {
            // Pass a lambda to handle key presses.
//            KeyboardLayout(onKeyPress = { key ->
//                onKeyPress(key)
//            })

            ModernKeyboard(
                onKeyPress = {
                    onKeyPress(it)
                },
                onDelete = { onKeyPress("DEL") },
                onSpace = { onKeyPress("SPACE") },
                onEnter = { onKeyPress("ENTER") },
                onCloseKeyboard = {
                    // check if keyboard is hide then show it or if it is show then hide it.

                    if (isKeyboardVisible) {
                        keyboardController?.show()
                    } else {
                        keyboardController?.hide()
                    }
                },
                onSwitchKeyboard = {
                    // Open system keyboard picker
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showInputMethodPicker()
                },
            )

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