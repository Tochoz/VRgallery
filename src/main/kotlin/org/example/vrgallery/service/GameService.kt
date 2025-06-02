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
import org.springframework.data.jpa.domain.AbstractPersistable_
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class GameService(
    private val gameRepository: GameRepository,
    private val mediaRepository: MediaRepository,
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

        game.medias = gameCreateDto.medias.map { it.toMedia(game).also { mediaRepository.save(it) } }.toMutableList()
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
