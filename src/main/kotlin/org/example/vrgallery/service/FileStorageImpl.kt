package org.example.vrgallery.service

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.util.ResourceUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.ResourceLoader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

@Service
class FileStorageImpl(private val resourceLoader: ResourceLoader) : FileStorage {

    val log = LoggerFactory.getLogger(this::class.java)
    val rootLocation: Path = Paths.get(ResourceUtils.getURL("storage").toURI())

    override fun store(file: MultipartFile, path: String, customName: String?): String {
        Files.copy(file.inputStream, Paths.get("filestorage/$path/${customName ?: file.originalFilename}"))
        return "/files/$path/${customName ?: file.originalFilename}"
    }

    override fun loadFile(filename: String): Resource {
        val file = ResourceUtils.getFile("filestorage$filename")
        val resource = resourceLoader.getResource(file.toURI().toString())
        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("Failed to load resource: $file")
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    override fun delete(filename: String) {
        val file = ResourceUtils.getFile("filestorage$filename")
        FileSystemUtils.deleteRecursively(file)
    }

    override fun init() {
        Files.createDirectory(rootLocation)
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1)
                .filter { path -> !path.equals(this.rootLocation) }
                .map(this.rootLocation::relativize)
    }
}