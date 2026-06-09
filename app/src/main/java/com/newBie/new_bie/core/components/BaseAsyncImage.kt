package com.newBie.new_bie.core.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import com.newBie.new_bie.R

@Composable
fun BaseAsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = RoundedCornerShape(0.dp)
) {

    // Compose 자체 무한 회전 애니메이션 정의
    val infiniteTransition = rememberInfiniteTransition(label = "loading_rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    SubcomposeAsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        contentScale = contentScale,
        // 로딩 중일 때 표시할 내용
        loading = {
            // 여기서 애니메이션이 없는 일반 벡터 아이콘을 보여주거나
            // Compose 자체 애니메이션을 적용할 수 있습니다.
            Image(
                painter = painterResource(R.drawable.ic_loading_icon), // animated-rotate가 아닌 일반 vector
                contentDescription = null,
                modifier = Modifier.rotate(angle)
            )
        },
        // 에러 발생 시 표시할 내용
        error = {
            Image(
                painter = painterResource(R.drawable.ic_broken_image),
                contentDescription = null
            )
        }
    )
}