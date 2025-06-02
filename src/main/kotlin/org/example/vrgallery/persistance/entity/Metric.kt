package org.example.vrgallery.persistance.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "metrics")
open class Metric {
    @Id
    @Column(name = "id_game", nullable = false)
    open var id: Int? = null

    @MapsId
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_game")
    open var game: Game? = null

    @ColumnDefault("0")
    @Column(name = "visits", nullable = false)
    open var visits: Int = 0

    @ColumnDefault("0")
    @Column(name = "downloads", nullable = false)
    open var downloads: Int = 0

    @ColumnDefault("now()")
    @Column(name = "last_change", nullable = false)
    open var lastChange: Instant? = null

    //TODO [Reverse Engineering] generate columns from DB
}