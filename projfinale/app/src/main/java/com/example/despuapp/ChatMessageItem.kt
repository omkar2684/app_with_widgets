package com.example.despuapp


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatMessageItem(message: Message) {
    val loggedInEmail = "your-email@example.com"
    val isOwnMessage = message.emailid == loggedInEmail
    val bubbleColor = if (isOwnMessage) MaterialTheme.colorScheme.primary else Color.LightGray
    val textColor = if (isOwnMessage) Color.White else Color.Black
    val alignment = if (isOwnMessage) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {
        if (!isOwnMessage) {
            Text(
                text = message.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Set the color to white
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        Box(
            modifier = Modifier
                .background(bubbleColor, MaterialTheme.shapes.medium)
                .padding(12.dp)
        ) {
            Text(text = message.msgContent, color = textColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMessageItemPreview() {
    val sampleMessage = Message(name = "John", emailid = "john@example.com", msgContent = "Hello!", course = "Course A")
    ChatMessageItem(message = sampleMessage)
}
