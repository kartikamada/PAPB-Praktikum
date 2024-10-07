package com.tifd.projectcomposed

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
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
    val context = LocalContext.current

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

    Box(modifier = Modifier.fillMaxSize().clickable {
        val intent = Intent(context, GithubProfile::class.java)
        context.startActivity(intent)
        Toast.makeText(context, "Navigating to GitHub Profile", Toast.LENGTH_SHORT).show()
    }
    ){
        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png")
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 70.dp)
        ) {
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

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFA5B6FD)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Logout")
            }
        }
    }
}
