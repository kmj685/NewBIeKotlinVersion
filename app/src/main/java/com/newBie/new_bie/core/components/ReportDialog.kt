package com.newBie.new_bie.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.newBie.new_bie.ui.theme.GridColor
import com.newBie.new_bie.ui.theme.OrangeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDialog(
    onConfirm: (category: String, content: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val reportCategory = listOf("영리목적 / 홍보성", "음란성 / 선정적 내용", "욕설 / 비방 / 혐오 표현", "도배 / 사기 / 허위 사실", "개인정보 노출 / 침해", "기타")
    val contentTextFieldState = rememberTextFieldState()
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(reportCategory[0]) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .widthIn(max = 320.dp)
                .wrapContentHeight()
                .background(color = GridColor, shape = RoundedCornerShape(8.dp))
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "신고 사유를 선택해주세요",
                    color = Color.White,
                    fontSize = 16.sp
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedCategory,
                        onValueChange = {},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedBorderColor = OrangeColor,
                            unfocusedBorderColor = Color.Gray,
                            focusedTrailingIconColor = OrangeColor,
                            unfocusedTrailingIconColor = Color.Gray
                        )
                    )
                    ExposedDropdownMenu(
                        modifier = Modifier.background(color = GridColor),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        reportCategory.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category,
                                        color = if (category == selectedCategory) OrangeColor else Color.White
                                    )
                                },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    state = contentTextFieldState,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    placeholder = { Text("자세한 사항을 적어주세요.", color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GridColor,
                        unfocusedContainerColor = GridColor
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
                    ) {
                        Text("취소")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirm(selectedCategory, contentTextFieldState.text.toString()) },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeColor, contentColor = Color.White)
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
fun ReportDialogPreview() {
    ReportDialog(
        onConfirm = { _, _ -> },
        onDismissRequest = {}
    )
}