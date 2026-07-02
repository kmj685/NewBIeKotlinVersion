package com.newBie.new_bie.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor

@Composable
fun CommonDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    askText: String
){
    Dialog(
        onDismissRequest = { onDismissRequest() },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .widthIn(max = 320.dp)
                .wrapContentHeight()
                .background(color = GridColor, shape = RoundedCornerShape(8.dp))
                .padding(24.dp)
        ){
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(askText, color = Color.White)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {onDismissRequest()},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("취소")
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {onConfirm()},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, showSystemUi = true)
@Composable
fun CommonDialogPreview(

){
    Dialog(
        onDismissRequest = { },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .widthIn(max = 320.dp)
                .wrapContentHeight()
                .background(color = GridColor, shape = RoundedCornerShape(8.dp))
                .padding(24.dp)
        ){
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("askText", color = Color.White)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("취소")
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}