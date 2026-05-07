import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.newBie.new_bie.R
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.random.Random

// 공 데이터 클래스
data class Ball(
    var pos: Offset,
    var vel: Offset,
    val radius: Float
)

@Composable
fun BouncingBallsBackground(obstacles: List<Rect>) {
    val currentObstacles by rememberUpdatedState(obstacles)
    val balls = remember { mutableStateListOf<Ball>() }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    // Canvas의 Window 기준 좌상단 오프셋 (장애물 좌표 변환에 사용)
    var canvasWindowOffset by remember { mutableStateOf(Offset.Zero) }

    // 40.dp를 px로 변환
    val density = LocalDensity.current
    val ballRadiusPx = with(density) { 20.dp.toPx() } // radius = 반지름(절반), size=40dp

    // 로고 이미지 로드
    val logoBitmap = ImageBitmap.imageResource(id = R.drawable.newbie_bgremove_logo)

    LaunchedEffect(canvasSize) {
        // 화면 크기가 결정되었고, 아직 공이 생성되지 않았을 때만 실행
        if (canvasSize != Size.Zero && balls.isEmpty()) {
            repeat(15) {
                balls.add(Ball(
                    pos = Offset(
                        x = Random.nextFloat() * canvasSize.width,
                        y = Random.nextFloat() * canvasSize.height
                    ),
                    vel = Offset(
                        x = Random.nextFloat() * 14f - 7f,
                        y = Random.nextFloat() * 14f - 7f
                    ),
                    radius = ballRadiusPx
                ))
            }
        }
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameNanos { _ ->
                if (canvasSize == Size.Zero || balls.isEmpty()) return@withFrameNanos

                // 장애물 좌표를 Canvas 로컬 좌표로 변환 (Window 기준 오프셋 차감)
                val localObstacles = currentObstacles.map { rect ->
                    Rect(
                        left   = rect.left   - canvasWindowOffset.x,
                        top    = rect.top    - canvasWindowOffset.y,
                        right  = rect.right  - canvasWindowOffset.x,
                        bottom = rect.bottom - canvasWindowOffset.y
                    )
                }

                for (i in balls.indices) {
                    val ball = balls[i]
                    var nextX = ball.pos.x + ball.vel.x
                    var nextY = ball.pos.y + ball.vel.y
                    var vx = ball.vel.x
                    var vy = ball.vel.y

                    // --- 1. 화면 경계(벽) 충돌 ---
                    if (nextX <= ball.radius) {
                        vx = abs(vx)
                        nextX = ball.radius
                    } else if (nextX >= canvasSize.width - ball.radius) {
                        vx = -abs(vx)
                        nextX = canvasSize.width - ball.radius
                    }

                    if (nextY <= ball.radius) {
                        vy = abs(vy)
                        nextY = ball.radius
                    } else if (nextY >= canvasSize.height - ball.radius) {
                        vy = -abs(vy)
                        nextY = canvasSize.height - ball.radius
                    }

                    // --- 2. 장애물(로고/버튼) 충돌 ---
                    localObstacles.forEach { rect ->
                        // 공의 AABB와 장애물이 겹치는지 확인
                        if (nextX + ball.radius > rect.left &&
                            nextX - ball.radius < rect.right &&
                            nextY + ball.radius > rect.top &&
                            nextY - ball.radius < rect.bottom
                        ) {
                            // 각 방향 침투 깊이 계산
                            val overlapL = (nextX + ball.radius) - rect.left  // 왼쪽 면 충돌
                            val overlapR = rect.right - (nextX - ball.radius)  // 오른쪽 면 충돌
                            val overlapT = (nextY + ball.radius) - rect.top    // 위쪽 면 충돌
                            val overlapB = rect.bottom - (nextY - ball.radius) // 아래쪽 면 충돌
                            val minOverlap = minOf(overlapL, overlapR, overlapT, overlapB)

                            // 가장 작은 침투 방향으로 튕기고, 공을 경계 밖으로 밀어냄
                            when (minOverlap) {
                                overlapL -> { vx = -abs(vx); nextX = rect.left - ball.radius }
                                overlapR -> { vx =  abs(vx); nextX = rect.right + ball.radius }
                                overlapT -> { vy = -abs(vy); nextY = rect.top - ball.radius }
                                overlapB -> { vy =  abs(vy); nextY = rect.bottom + ball.radius }
                            }
                        }
                    }

                    balls[i] = ball.copy(pos = Offset(nextX, nextY), vel = Offset(vx, vy))
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = it.toSize() }
            .onGloballyPositioned { coords ->
                // Canvas의 Window 기준 좌상단 좌표를 저장
                val bounds = coords.boundsInWindow()
                canvasWindowOffset = Offset(bounds.left, bounds.top)
            }
    ) {
        val imageSize = ballRadiusPx * 2f
        balls.forEach { ball ->
            drawImage(
                image = logoBitmap,
                dstOffset = androidx.compose.ui.unit.IntOffset(
                    x = (ball.pos.x - ballRadiusPx).toInt(),
                    y = (ball.pos.y - ballRadiusPx).toInt()
                ),
                dstSize = androidx.compose.ui.unit.IntSize(
                    width = imageSize.toInt(),
                    height = imageSize.toInt()
                )
            )
        }
    }
}