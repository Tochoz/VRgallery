package org.example.vrgallery.persistance.entity

import jakarta.persistence.*
import lombok.Getter
import lombok.Setter
import java.sql.Time
import java.sql.Timestamp
import java.time.Instant

@Entity
@Table(name = "games")
class Game (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_game", nullable = false)
    val id: Int,

    @Column
    val title: String,

    @Column(name = "title_eng")
    val titleEng: String? = null,

    @Column
    val description: String,

    @Column(name = "description_eng")
    val descriptionEng: String? = null,

    @Column
    var file: String,

    @Column(name = "is_third_party_link")
    val isThirdParty: Boolean,

    @Column(name = "added_date")
    val addedDate: Instant = Instant.now(),

    @Column

    var likes: Int = 0,

    @Column(name = "side_quest_link")
    val sideQuestLink: String? = null,

    @Column
    var preview: String = "/files/static/placeholder.png",

    )
{
    @OneToMany(mappedBy = "game")
    open var medias: MutableList<Media> = mutableListOf()

    @OneToOne(mappedBy = "game")
    open var metric: Metric? = null

}