package org.example.vrgallery.controller

import org.example.vrgallery.dto.GameCreateDto
import org.example.vrgallery.service.FileStorage
import org.example.vrgallery.service.GameService
import org.example.vrgallery.util.map
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.view.RedirectView


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
        return "admin/create"
    }

    @PostMapping("/create")
    fun createGame(
        @RequestParam("title") title: String,
        @RequestParam("titleEng") titleEng: String,
        @RequestParam("description") description: String,
        @RequestParam("descriptionEng") descriptionEng: String,
        @RequestParam("apkFile") apkFile: MultipartFile,
        @RequestParam(value = "fileLink", required = false) fileLink: String?,
        @RequestParam(value = "sideQuestLink", required = false) sideQuestLink: String?,
        @RequestParam("preview") preview: MultipartFile,
        @RequestParam(value = "previewLink", required = false) previewLink: String?,
        @RequestParam(value = "medias", required = false) medias: Array<MultipartFile>?,
        @RequestParam(value = "mediasLink", required = false) mediasLink: String?, model: Model
    ): RedirectView {

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
            ),
            apkFile = apkFile,
            preview = preview,
            medias = medias?.toList()
        )
        println(game)

        return RedirectView("/games")
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