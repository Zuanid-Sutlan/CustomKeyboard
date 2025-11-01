package com.example.customkeyboard.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The main Composable for the keyboard UI.
 * @param onKeyPress A lambda function to call when a key is pressed.
 */
@Composable
fun KeyboardLayout(onKeyPress: (String) -> Unit) {

    // Define our custom keyboard layout
    val row1 = "AZERTYUIOP"
    val row2 = "QSDFGHJKLM"
    val row3 = "WXCVBN"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF212121)) // Dark background for the keyboard
            .padding(horizontal = 2.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // First row of keys
        KeyboardRow(keys = row1.map { it.toString() }, onKeyPress = onKeyPress)

        // Second row of keys
        KeyboardRow(keys = row2.map { it.toString() }, onKeyPress = onKeyPress)

        // Third row of keys, with a "DEL" button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // Indent this row slightly
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            row3.forEach { char ->
                Key(
                    text = char.toString(),
                    modifier = Modifier.weight(1f),
                    onClick = { onKeyPress(char.toString()) }
                )
            }
            // Delete Key
            Key(
                icon = Icons.Default.ArrowBack,
                contentDescription = "Delete",
                modifier = Modifier.weight(1.5f), // Make delete slightly wider
                onClick = { onKeyPress("DEL") }
            )
        }

        // Fourth row (Space bar and Enter)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Space bar
            Key(
                icon = Icons.Default.SpaceBar,
                contentDescription = "Space",
                modifier = Modifier.weight(6f),
                onClick = { onKeyPress("SPACE") }
            )
            // Enter key
            Key(
                icon = Icons.Default.KeyboardReturn,
                contentDescription = "Enter",
                modifier = Modifier.weight(1.5f),
                onClick = { onKeyPress("ENTER") }
            )
        }
    }
}

/**
 * A Composable for a single row of keys.
 */
@Composable
private fun KeyboardRow(
    keys: List<String>,
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        keys.forEach { key ->
            Key(
                text = key,
                modifier = Modifier.weight(1f),
                onClick = { onKeyPress(key) }
            )
        }
    }
}

/**
 * A Composable for a single keyboard key.
 */
@Composable
private fun Key(
    text: String? = null,
    icon: ImageVector? = null,
    contentDescription: String? = text,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
