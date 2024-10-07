package com.tifd.projectcomposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel

class GithubProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectComposeDTheme {
                Surface {
                    GithubProfileScreen()
                }
            }
        }
    }
}

@Composable
fun GithubProfileScreen(viewModel: MainViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProfileUser("kartikamada")
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp
                    )
                }
                user != null -> {
                    DetailContent(user = user!!)
                }
            }
        }
    }
}

@Composable
fun DetailContent(user: Profile) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        )
        Text(
            text = user.name,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )
        Text(
            text = user.login,
            fontWeight = FontWeight.Light,
            fontSize = 18.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Followers: ${user.followers}",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
            Text(
                text = "Following: ${user.followingCount}",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )
        }
    }
}
