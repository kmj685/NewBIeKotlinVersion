package com.newBie.new_bie.features.profile.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.core.utils.Constants.TAG
import com.newBie.new_bie.features.post.domain.entities.PostWithProfileEntity
import com.newBie.new_bie.ui.theme.GridColor

@Composable
fun UserFeedGridItem(posts: List<PostWithProfileEntity>,
                     onPostClick: (Int) -> Unit = {},
                     onLoadMore: () -> Unit) {
    val gridState = rememberLazyGridState()

    // 스크롤 감지 로직
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = layoutInfo.totalItemsCount

                // 마지막에서 3번째 줄이 보일 때 다음 페이지 로드 (그리드가 3열이므로 3줄 = 12개 아이템)
                if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 6){
                    onLoadMore()
                }
            }
    }
    Log.d(TAG, "UserFeedGridScreen: posts size = ${posts.size}")

    LazyVerticalGrid(
        state = gridState, // 상태 연결
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            count = posts.size,
            key = { index -> posts[index].id },
        ) {index ->
            FeedItem(post = posts[index], onPostClick)
        }
    }
}

@Composable
fun FeedItem(post: PostWithProfileEntity, onPostClick: (Int) -> Unit){
    Box(
        modifier = Modifier
            .aspectRatio(0.7f)
            .background(GridColor)
            .clickable {onPostClick(post.id)},
        contentAlignment = Alignment.Center
    ){

        // 처음 보여줄 사진
        val displayImage = post.postImages.firstOrNull()?.imageUrl ?: post.imageUrl

        if (post.postImages.isNotEmpty()){
            BaseAsyncImage(
                model = displayImage,
                contentDescription = "피드 그리드 사진",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // 여러 장인 경우 오른쪽 상단에 여러장 아이콘 표시
            if (post.postImages.size > 1) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "여러장의 이미지",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp),
                    tint = Color.White.copy(0.8f)
                )
            }
        } else {
            Text("${post.title}",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                color = Color.White,
                )
        }
    }
}