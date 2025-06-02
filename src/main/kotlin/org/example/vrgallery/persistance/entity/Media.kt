package org.example.vrgallery.persistance.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.ColumnTransformer

@Entity
@Table(name = "medias")
open class Media (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int,

    @Column(name = "file")
    open var file: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @ColumnTransformer(write = "?::media_type")
    open var type: MediaType,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_game", nullable = false)
    open var game: Game,
){}