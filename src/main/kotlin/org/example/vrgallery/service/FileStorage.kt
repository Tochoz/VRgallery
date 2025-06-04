package org.example.vrgallery.service

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path
import java.util.stream.Stream

interface FileStorage {
    fun store(file: MultipartFile, path: String, customName: String? = null): String
    fun loadFile(filename: String): Resource
    fun delete(filename: String)
    fun deleteAll()
    fun init()
    fun loadFiles(): Stream<Path>
}