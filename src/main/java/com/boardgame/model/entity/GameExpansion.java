package com.boardgame.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Table(name = "game_expansions")
@Entity // Indique que cette classe correspond à une table dans la base de données
@Data // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor // creer un constructeur vide
@AllArgsConstructor // constructeur avec params
public class GameExpansion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expansion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;  // relation vers la table games

    @Column(name = "bgg_expansion_id")
    private Long bggExpansionId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "year_published")
    private Integer yearPublished;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
