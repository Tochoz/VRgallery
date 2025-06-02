package org.example.vrgallery.persistance.repository

import org.example.vrgallery.persistance.entity.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository: JpaRepository<Game, Int> {

}