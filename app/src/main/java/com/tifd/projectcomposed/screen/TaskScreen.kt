package com.tifd.projectcomposed.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.tifd.projectcomposed.data.model.local.Task
import com.tifd.projectcomposed.navigation.Screen
import com.tifd.projectcomposed.viewmodel.MainViewModel

@Composable
fun TaskScreen(
    navController: NavController,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var subject by rememberSaveable { mutableStateOf("") }
    var taskDetails by rememberSaveable { mutableStateOf("") }

    val tasksList by viewModel.tasksList.observeAsState(emptyList())
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Tugas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        Column {
            TextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Nama Mata Kuliah") },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
            )
            TextField(
                value = taskDetails,
                onValueChange = { taskDetails = it },
                label = { Text("Detail Tugas") },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp)
                )
            }

            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp),
            ) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Camera.route)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF605BBB)),
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Buka Kamera")
                }

                Button(
                    onClick = {
                        viewModel.addTask(subject, taskDetails, imageUri)
                        navController.currentBackStackEntry?.savedStateHandle?.remove<Uri>("capturedImageUri")
                        subject = ""
                        taskDetails = ""
                        imageUri = null
                    },
                    enabled = subject.isNotBlank() && taskDetails.isNotBlank() && (imageUri != null),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subject.isNotBlank() && taskDetails.isNotBlank() && (imageUri != null)) Color.White else Color.Gray
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        "Tambahkan Tugas",
                        color = Color(0xFFA6BEFD)
                    )
                }
            }
        }

        Text(
            text = "Daftar Tugas",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        tasksList.forEach { task ->
            TaskItem(task) {
                viewModel.toggleDone(task.id)
            }
        }
    }

    LaunchedEffect(navController) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Uri>("capturedImageUri")?.observeForever { uri ->
            imageUri = uri
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onMarkAsDone: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier.padding(bottom = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            task.imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Task Image",
                    modifier = Modifier
                        .height(100.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(
                    text = task.subject,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                Text(task.taskDetails)
            }

            IconButton(
                onClick = onMarkAsDone,
            ) {
                Icon(
                    imageVector = if (task.done) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                    contentDescription = "Mark as done",
                    tint = Color(0xFF605BBB),
                )
            }
        }
    }
}
