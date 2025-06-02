package org.example.vrgallery.service

import lombok.AllArgsConstructor
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
@AllArgsConstructor
class Storage(
    private val resourceLoader: ResourceLoader,
) {

    fun getResource(name: String): Resource = resourceLoader.getResource(name)
    fun saveResource(name: String) = resourceLoader.getResource(name)
}