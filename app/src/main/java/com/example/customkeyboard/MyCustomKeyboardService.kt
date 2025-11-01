package com.example.customkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.customkeyboard.presentation.ComposeHexadecimalKeyBoardView
import com.example.customkeyboard.presentation.KeyboardLayout
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme

/**
 * The main service for the custom keyboard.
 * This service is responsible for creating the keyboard UI and handling input.
 *
 * It implements LifecycleOwner, SavedStateRegistryOwner, and ViewModelStoreOwner to host Jetpack Compose content.
 */
class MyCustomKeyboardService : InputMethodService(), LifecycleOwner, SavedStateRegistryOwner, ViewModelStoreOwner {

    // --- Lifecycle, SavedStateRegistry, and ViewModelStore implementation ---
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val _viewModelStore = ViewModelStore()

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val lifecycle: Lifecycle
        get() =  lifecycleRegistry

    override val viewModelStore: ViewModelStore
        get() = _viewModelStore

    override fun onCreate() {
        savedStateRegistryController.performRestore(null)
        // Manually update the lifecycle state
        // We only move to ON_CREATE here.
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onCreate()
    }

    /**
     * This is called by the system to get the keyboard UI.
     * We return a ComposeView that hosts our @Composable keyboard.
     */
    /*override fun onCreateInputView(): View {
        val composeView = ComposeView(this).apply {
            val lifecycleOwner = context as? LifecycleOwner // Or create a custom one
            lifecycleOwner?.let { setViewTreeLifecycleOwner(it) }
        }

        // --- Wire up the Lifecycle, SavedState, and ViewModelStore owners ---
        // This is crucial for Compose to work correctly in a Service.
        composeView.setViewTreeLifecycleOwner(this)
        composeView.setViewTreeSavedStateRegistryOwner(this)
        composeView.setViewTreeViewModelStoreOwner(this)

        // Set the content to our custom Composable.
        composeView.setContent {
            CustomKeyboardTheme() { // Use the theme from your MainActivity
                // Pass a lambda to handle key presses.
                KeyboardLayout(onKeyPress = { key ->
                    handleKeyPress(key)
                })
            }
        }

        return composeView
    }*/
    override fun onCreateInputView(): View? {
        window?.window?.decorView?.let { decorView ->
            decorView.setViewTreeLifecycleOwner(this)
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
        return ComposeHexadecimalKeyBoardView(this, onKeyPress = { key ->
            handleKeyPress(key)
        })
    }

    /**
     * Called when the keyboard window is shown. This is where we should
     * move the lifecycle to ON_RESUME.
     */
    override fun onWindowShown() {
        super.onWindowShown()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    /**
     * Called when the keyboard window is hidden. This is where we should
     * move the lifecycle to ON_PAUSE and ON_STOP.
     */
    override fun onWindowHidden() {
        super.onWindowHidden()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    /**
     * Handles the logic for what to do when a key is pressed.
     */
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
            "SHIFT" -> {
                // Toggle the shift state
            }
            else -> {
                // Commit the character
                ic.commitText(key, 1)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Manually update the lifecycle state
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
    }
}
