package com.newBie.new_bie.features.post.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.newBie.new_bie.R
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth
import io.ktor.http.ContentType

@Composable
fun SmallProfileComponent(modifier: Modifier, imageUrl : String?, nickName : String?, introduce : String?, userId : String?, onImageClick : () -> Unit) {
    val imageSize = 60.dp

    val imageModifier = Modifier
        .size(imageSize)
        .clip(CircleShape)
        .clickable(enabled = userId != null) {
            onImageClick.invoke()
        }

    Row(horizontalArrangement = Arrangement.Center,
        modifier = modifier.padding(8.dp)
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "네트워크 이미지",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "유저 이미지",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1F)
        ) {
            if (nickName != null) {
                Text(nickName, color = OrangeColor)
            }
            Text(introduce ?: "", color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}