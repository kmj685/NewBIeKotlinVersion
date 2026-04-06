package com.newBie.new_bie.core.components

import android.content.Context
import android.net.Uri

// Content URI를 ByteArray로 변환하는 함수
fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
}