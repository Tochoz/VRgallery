package org.example.vrgallery.controller

import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.ClasspathLoader
import jakarta.servlet.http.HttpSession
import org.example.vrgallery.dto.LikeResponse
import org.example.vrgallery.service.GameService
import org.example.vrgallery.util.fillPebbleConst
import org.example.vrgallery.util.processLikedCookie
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.io.StringWriter
import java.time.Duration

@Controller
@RequestMapping("/")
class ViewController(
    private val gameService: GameService
) {
    private val pebbleEngine = PebbleEngine.Builder().loader(ClasspathLoader().apply {
        prefix = "templates/"
        suffix = ""
    }).build()

    @GetMapping()
    fun index(
        @RequestParam(required = false) lang: String? = null, model: Model,
        @CookieValue(name = "LIKED_ENTITIES", required = false) likedEntitiesString: String?,
        session: HttpSession,
    ): String {
        val likedEntities = processLikedCookie(likedEntitiesString)
        fillPebbleConst("main",lang, model, session)
        val isEngLocale = session.getAttribute("lang") == "en"

        val topGames = gameService.getTop(isEngLocale)
        model.addAttribute("topGames",
            topGames.map { it.copy(liked = it.id in likedEntities) }
        )

        return "index"
    }

    @GetMapping("/upload")
    fun uploadPage(): String {
        val template = pebbleEngine.getTemplate("uploadform.html")
        val writer = StringWriter()
        template.evaluate(writer)
        return writer.toString()
    }

    @GetMapping("/games")
    fun library(
        @RequestParam(required = false) lang: String? = null,
        @RequestParam(required = false) page: Int = 1, model: Model,
        @CookieValue(name = "LIKED_ENTITIES", required = false) likedEntitiesString: String?,
        @RequestParam(required = false) sort: String? = null,
        session: HttpSession,
    ): String {
        fillPebbleConst("library",lang, model, session)

        val isEngLocale = session.getAttribute("lang") == "en"

        val sortObj = when (sort){
            "recent" -> Sort.by(Sort.Direction.DESC, "addedDate")
            "liked" -> Sort.by(Sort.Direction.DESC, "likes")
            "viewed" -> Sort.by(Sort.Direction.DESC, "viewed")
            else -> Sort.by(Sort.Direction.DESC, "addedDate")
        }

        val likedEntities = processLikedCookie(likedEntitiesString)

        val (games, pageObj) = gameService.getGamePage(
            PageRequest.of(page-1, 16, sortObj),
            isEngLocale
        )

        model.addAttribute("games", games.map { it.copy(liked = it.id in likedEntities) })
        model.addAttribute("maxPages", pageObj.totalPages)
        sort?.let { model.addAttribute("sort", it) }
        model.addAttribute("page", page)
        return "library"
    }

    @GetMapping("/games/{id}")
    fun gameInfo(
        @RequestParam(required = false) lang: String? = null,
        @PathVariable(required = true) id: Int,
        @CookieValue(name = "LIKED_ENTITIES", required = false) likedEntitiesString: String?,
        model: Model,
        session: HttpSession
    ): String {
        fillPebbleConst("game",lang, model, session)
        val likedEntities = processLikedCookie(likedEntitiesString)
        val isEngLocale = session.getAttribute("lang") == "en"

        val game = gameService.getGameById(id, isEngLocale)

        model.addAttribute("game", game.copy(liked = game.id in likedEntities))
        return "game"
    }

    @PostMapping("/games/{id}/like")
    fun likeGame(
        @PathVariable(required = true) id: Int,
        @CookieValue(name = "LIKED_ENTITIES", required = false) likedEntitiesString: String?,
        session: HttpSession,
    ): ResponseEntity<LikeResponse> {
        val likedEntities = processLikedCookie(likedEntitiesString)

        val likesNow: Int = if (!likedEntities.contains(id)) {
            likedEntities.add(id)
            gameService.likeGame(id)
        } else
            gameService.getLikes(id)

        val newCookie = ResponseCookie.from("LIKED_ENTITIES", likedEntities.joinToString("|"))
            .maxAge(Duration.ofDays(360))
            .path("/")
//            .httpOnly(true)
            .secure(true)
            .build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, newCookie.toString())
            .body(LikeResponse(true, likesNow))
    }

    @PostMapping("/games/{id}/dislike")
    fun dislikeGame(
        @PathVariable(required = true) id: Int,
        @CookieValue(name = "LIKED_ENTITIES", required = false) likedEntitiesString: String?,
        session: HttpSession,
    ): ResponseEntity<LikeResponse> {
        val likedEntities = processLikedCookie(likedEntitiesString)

        val likesNow: Int = if (likedEntities.contains(id)) {
            likedEntities.remove(id)
            gameService.dislikeGame(id)
        } else
            gameService.getLikes(id)

        val newCookie = ResponseCookie.from("LIKED_ENTITIES", likedEntities.joinToString("|"))
            .maxAge(Duration.ofDays(360))
            .path("/")
//            .httpOnly(true)
            .secure(true)
            .build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, newCookie.toString())
            .body(LikeResponse(false, likesNow))
    }

}