package org.example.vrgallery.dto

data class GameCreateDto(
    val title: String,
    val titleEng: String? = null,
    val description: String,
    val descriptionEng: String? = null,
    val file: String? = null,
    val sideQuestLink: String? = null,
    val preview: String? = null,
    val medias: List<String>
) {

}