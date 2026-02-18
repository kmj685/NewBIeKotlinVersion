package com.newBie.new_bie.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

// 문자열이 제이슨 형태인지, 제이슨 배열 형태인지
fun String?.isJsonObject():Boolean {
    if (this?.startsWith("{") == true && this.endsWith("}")==true ) {
        return true
    } else {
        return false
    }
}

fun String?.isJsonArray(): Boolean {
    if (this?.startsWith("[") == true && this.endsWith("]")==true ) {
        return true
    } else {
        return false
    }
}

fun Context.findActivity(): Activity {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> throw IllegalStateException("Activity not found")
    }
}