package com.newBie.new_bie.features.notification.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import com.newBie.new_bie.R
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun NotificationItems(
    modifier: Modifier,
    imageUrl : String?,
    type: String,
    followingPostingUserName : String?,
    followerUserName : String?,
    isRead : Boolean,
    onClick : () -> Unit
    ) {
    val imageSize = 60.dp

    val imageModifier = Modifier
        .size(imageSize)
        .clip(CircleShape)

    Row(horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = {onClick.invoke()})
    ) {
        // 읽지 않음 표시 점
        if (!isRead) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(OrangeColor)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (imageUrl != null) {
            BaseAsyncImage(
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
            modifier = Modifier
                .weight(1F)
                .heightIn(min = 60.dp)
        ) {
            if (type == "NEW_POST"){
                Text("${followingPostingUserName}님이 새 게시글을 올렸습니다.", color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            if (type == "NEW_FOLLOWER"){
                Text("${followerUserName}님이 회원님을 팔로우하기 시작했습니다.", color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}