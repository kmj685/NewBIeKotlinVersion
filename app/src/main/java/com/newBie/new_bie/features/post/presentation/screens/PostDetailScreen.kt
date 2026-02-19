package com.newBie.new_bie.features.post.presentation.screens

import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.newBie.new_bie.core.components.TopBarTitleText
import com.newBie.new_bie.features.post.domain.entities.PostUserEntity
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.post.presentation.viewModels.HomeViewModel
import com.newBie.new_bie.features.post.presentation.viewModels.PostDetailViewModel
import com.newBie.new_bie.ui.theme.AppTextStyle
import io.github.jan.supabase.realtime.Column

@Composable
fun PostDetailScreen(modifier: Modifier = Modifier, navController: NavController,viewModel : PostDetailViewModel = viewModel<PostDetailViewModel>(), id: Int) {

    val post by viewModel.post.collectAsState()
    val user: PostUserEntity? = post?.user
    LaunchedEffect(Unit) {
        if (id != 0) {
            viewModel.fetchPost(id)
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBarTitleText("게시물")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                SmallProfileComponent(
                    modifier = Modifier.weight(1f),
                    imageUrl = user?.profileImage ?: "",
                    nickName = user?.nickName ?: "",
                    introduce = post?.createdAt,
                    userId = user?.id ?: "",
                    {}
                    )
            }
            Text(post?.title ?: "", style = AppTextStyle.Title, )
            Text(post?.content ?: "", style = AppTextStyle.Content, )

            if (post?.postImages?.isNotEmpty() == true && post != null){
                val pagerState = rememberPagerState(
                    pageCount = { post?.postImages?.size ?: 0 }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        val url = post?.postImages[page]?.imageUrl

                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop, // BoxFit.cover
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

        }

    }


}