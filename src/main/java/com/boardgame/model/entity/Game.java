package com.boardgame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "games")
@Entity // Indique que cette classe correspond à une table dans la base de données
@Data // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor // creer un constructeur vide
@AllArgsConstructor // constructeur avec params
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;
    private Long bggId;
    private String name;
    private String description;
    private String image;
    private int minPlayers;
    private int maxPlayers;
    private String duration;
    private String difficulty;
    private String category;
    private Integer yearPublished;
    private String publisher;
    private String designer;
    private Double bggRating;
    private Double weight;
    private Integer ageMin;
    private String gameType;
    private Boolean supportsCooperative;
    private Boolean supportsCompetitive;
    private Boolean supportsCampaign;
    private Boolean supportsHybrid;
    private Boolean hasExpansion;
    private Boolean hasCharacters;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameExpansion> expansions;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameCharacter> characters;
}
