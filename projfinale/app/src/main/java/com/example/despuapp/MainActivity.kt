package com.example.despuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.despuapp.ui.theme.DespuappTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DespuappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MessagesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MessagesScreen(modifier: Modifier = Modifier) {
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var selectedCourse by remember { mutableStateOf<String?>(null) }
    var selectedColor by remember { mutableStateOf(Color(0xFFF3AC56)) } // Default color for Course A

    // Firebase reference
    val database = FirebaseDatabase.getInstance().getReference("contactForm")

    // Fetch messages based on the selected course
    LaunchedEffect(selectedCourse) {
        if (selectedCourse != null) {
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fetchedMessages = mutableListOf<Message>()
                    for (messageSnapshot in snapshot.children) {
                        val message = messageSnapshot.getValue(Message::class.java)
                        if (message != null) {
                            // Include messages sent to "All Courses" or specific course
                            if (message.course == "All Courses" || message.course == selectedCourse) {
                                fetchedMessages.add(message)
                            }
                        }
                    }
                    messages = fetchedMessages
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    // Change the color dynamically based on the selected course
    when (selectedCourse) {
        "Course A" -> selectedColor = Color(0xFFB725C3) // Example color for Course A
        "Course B" -> selectedColor = Color(0xFF1E9433) // Example color for Course B
        "Course C" -> selectedColor = Color(0xFF233DB7) // Example color for Course C
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(selectedColor) // Dynamically change the background color
    ) {
        // Buttons to select course placed horizontally
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // List of courses as buttons
                listOf("Course A", "Course B", "Course C").forEach { course ->
                    Button(onClick = { selectedCourse = course }) {
                        Text(text = course)
                    }
                }
            }

            // Display message "Select a course" if no course is selected
            if (selectedCourse == null) {
                Text(
                    text = "Select a course",
                    modifier = Modifier.padding(top = 32.dp), // Add some space from buttons
                    color = Color.White
                )
            }
        }

        // Display the messages only after a course is selected
        if (selectedCourse != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp) // Add padding to avoid overlap with buttons
            ) {
                items(messages) { message ->
                    ChatMessageItem(message = message)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MessagesScreenPreview() {
    DespuappTheme {
        MessagesScreen()
    }
}
