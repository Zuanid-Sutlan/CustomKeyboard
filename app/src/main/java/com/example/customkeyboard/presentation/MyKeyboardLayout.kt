package com.example.customkeyboard.presentation

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customkeyboard.ui.theme.CustomKeyboardTheme

//@Preview( showSystemUi = true,
//    uiMode = Configuration.UI_MODE_NIGHT_UNDEFINED, showBackground = false
//)
@Composable
fun MyKeyboardLayout(modifier: Modifier = Modifier) {

    var keyboardMode by remember { mutableStateOf(KeyboardMode.UPPERCASE) }

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
        listOf("@", "#", "$", "%", "&", "*", "(", ")", "-", "_"),
        listOf("+", "=", "/", ":", ";", ",", ".", "?", "!")
    )

    CustomKeyboardTheme(darkTheme = true) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.4f)
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)) {
                    KeyRow(modifier = Modifier.weight(1f), item = lowercaseKeys[0]) { }
                    KeyRow(modifier = Modifier.weight(1f), item = lowercaseKeys[1]) { }
                    KeyRow(modifier = Modifier.weight(1f), item = lowercaseKeys[2]) { }
                    BottomRow(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BottomRow(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

    }
}
@Composable
private fun KeyRow(modifier: Modifier, item: List<String>, onKeyClick: (String) -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
        item.forEach { string ->
            Key(
                modifier = Modifier.weight(1f),
                text = string,
                onClick = { onKeyClick(string) }
            )
        }
    }
}

@Composable
private fun Key(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    contentDescription: String? = text,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 5.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
