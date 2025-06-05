package org.example.vrgallery.dto

import java.time.Year

data class GameCreateDto(
    val title: String,
    val titleEng: String? = null,
    val description: String,
    val year: Int?,
    val descriptionEng: String? = null,
    val file: String? = null,
    val sideQuestLink: String? = null,
    val preview: String? = null,
    val medias: List<String>
) {

}