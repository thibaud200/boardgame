package com.boardgame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "players")
@Entity // Indique que cette classe correspond à une table dans la base de données
@Data // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor // creer un constructeur vide
@AllArgsConstructor // constructeur avec params
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    private String playerName;
    private String avatar;
    private Integer gamesPlayed;
    private Integer wins;
    private Integer totalScore;
    private Double averageScore;
    private String favoriteGame;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<SessionPlayer> sessions;
}
