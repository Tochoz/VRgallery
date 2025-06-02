package org.example.vrgallery.controller

import org.example.vrgallery.service.FileStorage
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.MediaType
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController(value = "files")
@RequestMapping("")
class FileController(private val fileStorage: FileStorage) {
    @GetMapping("/files/{*filename}")
    fun download(@PathVariable filename: String): ResponseEntity<Resource> {
        val mediaType = when (filename.split('.').last()){
            "png" -> MediaType.IMAGE_PNG
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "apk" -> MediaType.parseMediaType("application/vnd.android.package-archive")
            "css" ->MediaType.parseMediaType("text/css")
            "js" -> MediaType.parseMediaType("text/javascript")
            "svg" -> MediaType.parseMediaType("image/svg+xml")
            else -> MediaType.APPLICATION_OCTET_STREAM
        }
        val file = fileStorage.loadFile(filename)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename?.split('\\')?.first() + "\"")
            .contentType(mediaType)
            .body(file)
    }

    @PostMapping("/upload")
    fun upload(@RequestParam("uploadfile") file: MultipartFile, model: Model): ResponseEntity<Boolean> {
        fileStorage.store(file, "apk")
        return ResponseEntity.ok(true)
    }
}