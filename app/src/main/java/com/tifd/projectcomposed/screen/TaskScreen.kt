package com.tifd.projectcomposed.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tifd.projectcomposed.data.model.local.Task
import com.tifd.projectcomposed.viewmodel.MainViewModel
import javax.security.auth.Subject

@Composable
fun TaskScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var subject by remember { mutableStateOf("") }
    var taskDetails by remember { mutableStateOf("") }

    val tasksList by viewModel.tasksList.observeAsState(emptyList())

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
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
            )
            TextField(
                value = taskDetails,
                onValueChange = { taskDetails = it },
                label = { Text("Detail Tugas") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.addTask(subject, taskDetails) },
                enabled = subject.isNotBlank() && taskDetails.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (subject.isNotBlank() && taskDetails.isNotBlank()) Color.White else Color.Gray
                ),
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Tambahkan Tugas",
                    color = Color(0xFFA6BEFD)
                )
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
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Column {
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
                    tint = Color(0xFF039be5),
                )
            }
        }
    }
}
