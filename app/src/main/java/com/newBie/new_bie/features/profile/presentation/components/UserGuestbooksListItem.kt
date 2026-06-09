package com.newBie.new_bie.features.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.newBie.new_bie.core.components.BaseAsyncImage
import com.newBie.new_bie.features.profile.domain.entities.GuestbooksEntity
import com.newBie.new_bie.ui.theme.GridColor

@Composable
fun UserGuestbookListItem(
    guestbooks: List<GuestbooksEntity>,
    onLoadMore: () -> Unit,
    // Int: 클릭 시 Int 값(guestbooks.id)를 하나 던져 주겠다는 뜻 -> 이 후에 클릭시 navigate 경로 설정시 Int 넘겨주기 위해서
    onClick: (Int) -> Unit){
    val lazyState = rememberLazyListState()

    // 스크롤 감지 로직
    LaunchedEffect(lazyState) {
        snapshotFlow { lazyState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                val totalItems = layoutInfo.totalItemsCount

                // 마지막에서 3번째 줄이 보일 때 다음 페이지 로드 (그리드가 3열이므로 3줄 = 12개 아이템)
                if (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 6){

                    onLoadMore.invoke()
                }
            }
    }

    LazyColumn(
        state = lazyState,
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            count = guestbooks.size,
            key = { index -> guestbooks[index].id }
        ){index ->
            GuestbooksLazyColumnItem(
                guestbooks = guestbooks[index],
                onClick = onClick
            )
        }
    }
}

@Composable
fun GuestbooksLazyColumnItem(guestbooks: GuestbooksEntity, onClick: (Int) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GridColor)
            .clickable{onClick(guestbooks.id)}
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GuestbooksInfo(
                guestbooks = guestbooks,
                model = guestbooks.imageUrl
            )
        }
    }
}

@Composable
// RowScope.를 붙여야 이 함수 내부가 Row 환경임을 보장하여 weight을 붙일 수 있다.
fun RowScope.GuestbooksInfo(guestbooks: GuestbooksEntity, model: String?){
    if (guestbooks.imageUrl != null){
        BaseAsyncImage(
            model = model,
            contentDescription = "방명록 사진",
            modifier = Modifier.size(70.dp),
            shape = RoundedCornerShape(12.dp),
            contentScale = ContentScale.Crop
        )
    }
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            guestbooks.content,
            color = Color.White,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        guestbooks.senderId.nickName?.let {
            Text(
                it,
                color = Color.LightGray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
    Column(
        modifier = Modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ChatBubble,
            tint = Color.White,
            contentDescription = "방명록 댓글",
            modifier = Modifier
                .size(20.dp)
        )
        Text(
            guestbooks.commentsCount.toString(),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}