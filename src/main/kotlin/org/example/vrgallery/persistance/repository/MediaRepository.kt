package org.example.vrgallery.persistance.repository

import org.example.vrgallery.persistance.entity.Game
import org.example.vrgallery.persistance.entity.Media
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MediaRepository: JpaRepository<Media, Int> {
    fun findAllByGame_Id(int: Int): MutableList<Media>

}