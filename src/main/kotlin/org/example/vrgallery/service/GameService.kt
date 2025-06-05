package org.example.vrgallery.service

import jakarta.transaction.Transactional
import org.example.vrgallery.dto.GameCreateDto
import org.example.vrgallery.dto.GameDetailsDto
import org.example.vrgallery.dto.GamePrevDto
import org.example.vrgallery.mapper.toDetails
import org.example.vrgallery.mapper.toGame
import org.example.vrgallery.mapper.toMedia
import org.example.vrgallery.mapper.toPreview
import org.example.vrgallery.persistance.entity.Game
import org.example.vrgallery.persistance.repository.GameRepository
import org.example.vrgallery.persistance.repository.MediaRepository
import org.hibernate.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.jvm.optionals.getOrNull


@Service
class GameService(
    private val gameRepository: GameRepository,
    private val mediaRepository: MediaRepository,
    private val fileStorage: FileStorage,
    ) {
    fun getGameById(id: Int, isEng: Boolean = false): GameDetailsDto{
        val game = gameRepository.findById(id)

        if (game.isEmpty) throw ObjectNotFoundException("Игра не найдена", id)

        return game.get().toDetails(isEng)
    }

    fun getGamePage(page: PageRequest, isEng: Boolean = false): Pair<List<GamePrevDto>, Page<Game>> {
        val games = gameRepository.findAll(page)

        return games.content.map { it.toPreview(isEng) } to games
    }

    @Transactional
    fun createGame(
        gameCreateDto: GameCreateDto,
        apkFile: MultipartFile? = null,
        preview: MultipartFile? = null,
        medias: List<MultipartFile>? = null,
    ): Game {

        var game = gameCreateDto.toGame()
        game = gameRepository.save(game)
        apkFile?.let {
            fileStorage.store(apkFile, "apk", "${game.id}_${apkFile.originalFilename}")
        }?.also { game.file = it }

        preview?.let {
            fileStorage.store(preview, "game_media", "${game.id}_preview_${preview.originalFilename}")
        }?.also { game.preview = it }

        game.medias = if (medias != null)
            medias.mapIndexed { index, media ->
                fileStorage.store(media, "game_media", "${game.id}_${index}_${media.originalFilename}")
            }.map { it.toMedia(game).also { mediaRepository.save(it) } }.toMutableList()
        else
            gameCreateDto.medias.map { it.toMedia(game).also { mediaRepository.save(it) } }.toMutableList()
        gameRepository.save(game)

        return game
    }

    @Transactional
    fun likeGame(id: Int): Int{
        val game: Game = gameRepository.findById(id).orElseThrow {
            IllegalArgumentException(
                "Entity not found"
            )
        }
        game.likes++
        gameRepository.save(game)
        return game.likes
    }

    @Transactional
    fun getLikes(id: Int): Int{
        val game: Game = gameRepository.findById(id).orElseThrow {
            IllegalArgumentException(
                "Entity not found"
            )
        }
        return game.likes
    }

    @Transactional
    fun getTop(isEng: Boolean = false): List<GamePrevDto>{
        val games: List<Game> = gameRepository.findAll(
            PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC,"likes"))
        ).content
        return games.map { it.toPreview(isEng) }
    }

    @Transactional
    fun deleteGame(id: Int): Unit {
        val game: Game? = gameRepository.findById(id).getOrNull()
        if (game != null) {
            game.file.takeIf { it.startsWith("/") }?.let {
                fileStorage.delete(it.removePrefix("/files"))
            }
            game.preview.takeIf { it.startsWith("/") && it != "/files/static/placeholder.png" }?.let {
                fileStorage.delete(it.removePrefix("/files"))
            }
            game.medias.forEach { media ->
                media.file.takeIf { it.startsWith("/") && it != "/files/static/placeholder.png"  }?.let {
                    fileStorage.delete(it.removePrefix("/files"))
                }
                mediaRepository.delete(media)
            }
            gameRepository.delete(game)
        }
    }

    @Transactional
    fun dislikeGame(id: Int): Int{
        val game: Game = gameRepository.findById(id).orElseThrow {
            IllegalArgumentException(
                "Entity not found"
            )
        }
        if (game.likes > 0) game.likes--
        gameRepository.save(game)
        return game.likes
    }
}
