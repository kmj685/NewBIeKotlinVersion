package com.newBie.new_bie.core.components

import android.util.Patterns
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

@Composable
fun LinkifyText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default
) {
    val annotatedString = buildAnnotatedString {
        val matcher = Patterns.WEB_URL.matcher(text)
        var lastIndex = 0

        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            val url = matcher.group() ?: ""

            // 1. 링크 앞부분의 일반 텍스트 추가
            append(text.substring(lastIndex, start))

            // 2. 방어 코드: http가 없으면 붙여주기
            val fullUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                "http://$url"
            } else {
                url
            }

            // 3. 최신 방식: LinkAnnotation.Url 사용
            val link = LinkAnnotation.Url(
                url = fullUrl,
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = Color(0xFF64B5F6), // 파란색 링크
                        textDecoration = TextDecoration.Underline
                    )
                )
            )

            // 4. 링크 스타일과 클릭 이벤트를 텍스트에 적용
            withLink(link) {
                append(url)
            }
            lastIndex = end
        }

        // 5. 마지막 링크 이후에 남은 일반 텍스트 마저 추가
        append(text.substring(lastIndex))
    }

    // 6. ClickableText 대신 일반 Text 사용! (내부에서 알아서 클릭 처리됨)
    Text(
        text = annotatedString,
        modifier = modifier,
        style = style
    )
}