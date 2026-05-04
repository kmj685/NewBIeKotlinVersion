package com.newBie.new_bie.features.profile.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.features.post.domain.entities.UserEntity
import com.newBie.new_bie.features.post.presentation.components.SmallProfileComponent
import com.newBie.new_bie.features.profile.domain.entities.FollowEntity
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun FollowItem(followData: FollowEntity?, onButtonClick: () -> Unit, navController: NavController, isFollowing: Boolean, myId: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필은 가능한 넓게 (이미 내부에서 weight(1f)가 있음)
        Box(modifier = Modifier.weight(1f)) {
            SmallProfileComponent(
                modifier = Modifier,
                imageUrl = followData?.users?.profileImage,
                nickName = followData?.users?.nickName,
                introduce = followData?.users?.introduction,
                userId = followData?.users?.id,
                onImageClick = { navController.navigate("${Routes.MY_PROFILE}/${followData?.users?.id}")}
            )
        }

        if (myId != followData?.users?.id) {
            Button(
                onClick = { onButtonClick.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFollowing) Color.Gray else OrangeColor,
                    contentColor = Color.White,
                )

            ) {
                Text(if (isFollowing)"팔로잉" else "팔로우", fontSize = 15.sp)
            }
        }
    }
}