package com.sora25.app2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sora25.app2.R
import com.sora25.app2.data.GoogleAuth
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Box(Modifier.fillMaxSize()) {
        // Обои на заднем плане: файл res/drawable/login_bg.* (замени на свой)
        Image(
            painterResource(R.drawable.login_bg), null,
            Modifier.fillMaxSize(), contentScale = ContentScale.Crop,
        )
        Column(
            Modifier.fillMaxSize().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(painterResource(R.drawable.logo_cloud), null, Modifier.size(140.dp))
            Spacer(Modifier.height(12.dp))
            Image(painterResource(R.drawable.logo_text), null, Modifier.width(220.dp))
            Spacer(Modifier.height(10.dp))
            Text(
                "Превращай идеи в видео",
                color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp, textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = {
                    loading = true; error = null
                    scope.launch {
                        GoogleAuth.signIn(ctx).onFailure { error = it.message ?: "Не удалось войти" }
                        loading = false
                    }
                },
                enabled = !loading,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth().height(54.dp),
            ) {
                if (loading) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = Color.Black)
                else Text("Войти через Google", fontSize = 16.sp)
            }
            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Color(0xFFFFD6D6), fontSize = 13.sp, textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(0.95f))
            }
        }
    }
}
