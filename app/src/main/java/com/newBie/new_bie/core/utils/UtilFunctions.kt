package com.newBie.new_bie.core.utils

fun getRange(currentIndex : Int, perPage : Int = 5) : String {
    var startIndex : Int = currentIndex - 1
    var endIndex : Int = perPage - 1

    if (currentIndex != 1) {
        endIndex = (currentIndex * perPage) - 1
        startIndex = (currentIndex - 1) * perPage
    }
    val range : String = "${startIndex}-${endIndex}"
    return range
}