package com.tifd.projectcomposed

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme

class ViewScheduleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectComposeDTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ViewScheduleScreen(onLogout = {
                        Firebase.auth.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    })
                }
            }
        }
    }
}

@Composable
fun ViewScheduleScreen(onLogout: () -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    var scheduleList by remember { mutableStateOf(listOf<List<String>>()) }

    LaunchedEffect(Unit) {
        firestore.collection("schedules")
            .get()
            .addOnSuccessListener { result ->
                val tempList = mutableListOf<List<String>>()
                for (document in result) {
                    val day = document.getString("day") ?: ""
                    val courseName = document.getString("courseName") ?: ""
                    val timeEnd = document.getString("timeEnd") ?: ""
                    val timeStart = document.getString("timeStart") ?: ""
                    val room = document.getString("room") ?: ""
                    tempList.add(listOf(day, courseName, timeStart, timeEnd, room))
                }
                scheduleList = tempList
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFA5B6FD)
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp)
            ) {
                Text("Logout")
            }

            scheduleList.forEach { row ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = Color(0xFFFFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { item ->
                            Text(
                                text = item,
                                modifier = Modifier.weight(1f),
                                fontSize = 12.sp,
                                color = Color(0xFF605BBB)
                            )
                        }
                    }
                }
            }
        }
    }
}
