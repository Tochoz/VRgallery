package org.example.vrgallery.dto

data class GamePrevDto(
    val id: Int,
    val title: String,
    val likes: Int = 0,
    val preview: String = "/files/static/placeholder.png",
    var liked: Boolean = false,
)