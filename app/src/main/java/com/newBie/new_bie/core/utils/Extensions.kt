package com.newBie.new_bie.core.utils

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