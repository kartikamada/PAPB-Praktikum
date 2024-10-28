package com.tifd.projectcomposed.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.tifd.projectcomposed.data.model.network.Profile
import com.tifd.projectcomposed.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val activity = LocalContext.current as Activity

    LaunchedEffect(Unit) {
        viewModel.getProfileUser("kartikamada")
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Profil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        user?.let { DetailContent(it, activity) }

        Button(
            onClick = {
                Firebase.auth.signOut()
                activity.finish()
            },
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

@Composable
fun DetailContent(user: Profile, activity: Activity) {
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
                .clickable {
                    // Aksi ketika gambar ditekan
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/${user.login}"))
                    activity.startActivity(intent)
                }
        )
        Text(
            text = user.name ?: user.login,
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
