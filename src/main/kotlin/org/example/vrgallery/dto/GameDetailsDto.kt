package org.example.vrgallery.dto

import org.example.vrgallery.persistance.entity.Media

data class GameDetailsDto(
    val id: Int,
    val title: String,
    val description: String,
    val likes: Int = 0,
    val file: String,
    val sideQuestLink: String?,
    val preview: String = "/files/static/placeholder.png",
    val medias: MutableList<Media>,
)