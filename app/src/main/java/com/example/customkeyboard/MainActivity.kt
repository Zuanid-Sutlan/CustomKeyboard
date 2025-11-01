package com.example.customkeyboard

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CustomKeyboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KeyboardSettingsScreen(modifier = Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun KeyboardSettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val keyboardId =
        "com.example.mycustomkeyboard/.MyCustomKeyboardService" // Must match manifest

    // State to track if the keyboard is enabled and selected
    var isEnabled by remember { mutableStateOf(false) }
    var isSelected by remember { mutableStateOf(false) }

    // This effect checks the status whenever the composable is (re)composed
    LaunchedEffect(Unit) {
        isEnabled = imm.enabledInputMethodList.any { it.id == keyboardId }
        val currentMethod = Settings.Secure.getString(context.contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        isSelected = currentMethod == keyboardId
    }

    // Add a Lifecycle observer to re-check when the user returns to the app
    /*val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Re-check the settings when the app is resumed
                isEnabled = imm.enabledInputMethodList.any { it.id == keyboardId }
                val currentMethod = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.DEFAULT_INPUT_METHOD
                )
                isSelected = currentMethod == keyboardId
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Remove the observer when the composable is disposed
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }*/

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var testField by remember { mutableStateOf("") }
        OutlinedTextField(
            value = testField,
            onValueChange = {
                testField = it
            }
        )
        Image(painter = painterResource(R.drawable.a), contentDescription = null)
        Spacer(modifier = Modifier.height(56.dp))

        Text(
            text = "Custom Keyboard Setup",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Follow these steps to use your keyboard:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Step 1: Enable the keyboard
        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                context.startActivity(intent)
            },
            enabled = !isEnabled
        ) {
            Text(if (isEnabled) "1. Keyboard Enabled" else "1. Enable Keyboard in Settings")
        }
        if (isEnabled) {
            Text("✅ Enabled", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 2: Select the keyboard
        Button(
            onClick = {
//                imm.showInputMethodPicker()
                (context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()
            },
//            enabled = isEnabled // Can only select if it's enabled
        ) {
            Text(if (isSelected) "2. Keyboard Selected" else "2. Select Keyboard")
        }
        if (isSelected) {
            Text("✅ Active", color = MaterialTheme.colorScheme.primary)
        }
    }
}
