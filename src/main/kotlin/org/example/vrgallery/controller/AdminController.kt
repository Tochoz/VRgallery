package org.example.vrgallery.controller

import org.example.vrgallery.dto.GameCreateDto
import org.example.vrgallery.service.FileStorage
import org.example.vrgallery.service.GameService
import org.example.vrgallery.util.map
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView
import java.time.Year


@Controller
@RequestMapping("/manage")
class AdminController(
    private val fileStorage: FileStorage,
    private val gameService: GameService,
) {
    @GetMapping("/")
    fun index(model: ModelMap): String {
        model.addAttribute(
            "isAdmin",
            true
        )
        return "admin/index"
    }
    @GetMapping("/create")
    fun createForm(model: ModelMap): String {
        model.addAttribute(
            "isAdmin",
            true
        )
        model.addAttribute("year", Year.now().value)
        return "admin/create"
    }

    @PostMapping("/create")
    fun createGame(
        @RequestParam("title") title: String,
        @RequestParam("titleEng") titleEng: String,
        @RequestParam("description") description: String,
        @RequestParam("descriptionEng") descriptionEng: String,
        @RequestParam("year") year: Int,
        @RequestParam("fileLink", required = false) fileLink: String?,
        @RequestParam("sideQuestLink", required = false) sideQuestLink: String?,
        @RequestParam("previewLink", required = false) previewLink: String?,
        @RequestParam("mediasLink", required = false) mediasLink: String?, model: Model,
        @RequestParam("apkFile", required = false) apkFile: MultipartFile?,
        @RequestParam("preview", required = false) preview: MultipartFile?,
        @RequestParam("medias", required = false) medias: List<MultipartFile>?,
        @RequestParam("mediasOrder", required = true) mediasOrder: String,

    ): RedirectView {
        val orderedMedias = mediasOrder.split(",").map { orderedName ->  medias?.find{ it.originalFilename == orderedName }!!  }

        val game = gameService.createGame(
            GameCreateDto(
                title = title,
                titleEng = titleEng.isNotBlank().map(titleEng, null),
                description = description,
                descriptionEng = descriptionEng.isNotBlank().map(descriptionEng, null),
                file = fileLink,
                sideQuestLink = sideQuestLink,
                preview = previewLink?.isNotBlank()?.map(previewLink, null),
                medias = mediasLink?.split(';') ?: emptyList(),
                year = year
            ),
            apkFile = apkFile,
            preview = preview,
            medias = orderedMedias
        )
        println(game)

        return RedirectView("/games")
    }

    @DeleteMapping("/games/{id}")
    fun deleteGame(@PathVariable id: Int): ResponseEntity<Any>{
        gameService.deleteGame(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/games/{id}")
    fun gameEditForm(@PathVariable id: Int, model: ModelMap): String {
        model.addAttribute(
            "isAdmin",
            true
        )
        return "admin"
    }
}