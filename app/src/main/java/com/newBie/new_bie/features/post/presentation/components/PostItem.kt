package com.newBie.new_bie.features.post.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.toKoreaLocalDateTime
import com.newBie.new_bie.core.utils.toTimeAgo
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostItem(post : PostWithProfileEntity, onDelete : () -> Unit, onLike : () -> Unit, onClick : (Int) -> Unit = {}, onComments: () -> Unit = {}) {

    val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12))
            .background(color = GridColor)
            .padding(12.dp)
            .clickable(onClick = {onClick.invoke(post.id)})
    ) {
        Row(
            modifier = Modifier
        ) {
            SmallProfileComponent(
                modifier = Modifier
                    .weight(1f),
                imageUrl = post.user?.profileImage,
                nickName = post.user?.nickName ?: "",
                introduce = post.createdAt.toKoreaLocalDateTime().toTimeAgo(),
                onImageClick = {},
                userId = post.user?.id
                )

//            if (post.user?.id != currentUserId) {
//                PostMenu(
//                    onReport = { /* TODO */ },
//                    onBlock = {
//                        post.user?.id?.let { onBlockUser(it) }
//                    }
//                )
//            }

        }
        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 제목
        Text(
            text = post.title ?: "제목 없음",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔹 이미지 리스트
        if (!post.postImages.isNullOrEmpty()) {
            LazyRow {
                items(post.postImages) { image ->
                    AsyncImage(
                        model = image.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // 🔹 내용
        Text(
            text = post.content ?: "내용 없음",
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

//         🔹 카테고리
        if (!post.categories.isNullOrEmpty()) {
            LazyRow {
                items(post.categories) { category ->
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(OrangeColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category.categoryType.typeTitle,
//                            text = "카테고리",
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // 🔹 좋아요 + 댓글
        Row {
            Row(
                modifier = Modifier.clickable { onLike() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (post.isLiked == true)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (post.isLiked == true) Color.Red else Color.White
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${post.likesCount}",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                modifier = Modifier.clickable{ onComments()},
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Comment,
                    contentDescription = null,
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${post.commentsCount}",
                    color = Color.White
                )
            }
        }
    }
}