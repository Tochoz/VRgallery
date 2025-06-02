package org.example.vrgallery.mapper

import org.example.vrgallery.dto.GameCreateDto
import org.example.vrgallery.dto.GameDetailsDto
import org.example.vrgallery.dto.GamePrevDto
import org.example.vrgallery.persistance.entity.Game
import org.example.vrgallery.persistance.entity.Media
import org.example.vrgallery.persistance.entity.MediaType
import org.example.vrgallery.util.map

fun Game.toPreview(isEngLocale: Boolean = false) = GamePrevDto(
    id = id,
    title = (isEngLocale && titleEng != null).map(titleEng!!, title),
    likes = likes,
    preview = preview
)

fun Game.toDetails(isEngLocale: Boolean = false) = GameDetailsDto(
    id = id,
    title = (isEngLocale && titleEng != null).map(titleEng!!, title),
    likes = likes,
    preview = preview,
    description = (isEngLocale && descriptionEng != null).map(descriptionEng!!, description),
    file = file,
    sideQuestLink = sideQuestLink,
    medias = medias,
)

fun GameCreateDto.toGame(
    uploadedApk: String? = null,
    uploadedPreview: String? = null,
    uploadedMedias: List<String>? = null,
    ) = Game(
    id = 0,
    title = title,

    titleEng = titleEng ?: title,
    description = description,
    descriptionEng = descriptionEng ?: description,
    file = file ?: uploadedApk ?: "",
    isThirdParty = false,
    sideQuestLink = sideQuestLink,
    preview = preview ?: uploadedPreview ?: "/files/static/placeholder.png",
)

fun String.toMedia(game: Game) = Media(
    id = 0,
    file = this,
    type = MediaType.image,
    game = game
)