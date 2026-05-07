package com.newBie.new_bie.core.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.newBie.new_bie.core.managers.SupabaseManager
import com.newBie.new_bie.core.utils.Constants
import com.newBie.new_bie.core.utils.Routes
import com.newBie.new_bie.ui.theme.OrangeColor
import io.github.jan.supabase.auth.auth

@Composable
fun TopBarLayout(title: String,
                 moreVert: Boolean = false, targetId: String? = null, focusManager: FocusManager? = null,
                 setting: Boolean = false,
                 navController: NavController? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = OrangeColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* 알림 */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "알림", tint = OrangeColor)
            }
            var expanded by remember { mutableStateOf(false) }
            val currentUserId = SupabaseManager.supabase.auth.currentUserOrNull()?.id

            if(moreVert && targetId != null && targetId != currentUserId){
                Box {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "유저 동작 설정",
                            tint = OrangeColor
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                            focusManager?.clearFocus()
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("신고") },
                            onClick = { /* Do something... */
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("차단") },
                            onClick = { /* Do something... */
                                expanded = false
                            }
                        )
                    }
                }
            }
            if (setting && targetId == null || targetId == currentUserId){
                IconButton(onClick = { navController?.navigate("${Routes.MY_PROFILE}/${Routes.SETTING}") }) {
                    Icon(Icons.Default.Settings, "설정 창 이동", tint = OrangeColor)
                }
            }
        }
    }
}