package com.boardgame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "session_players") // Nom explicite de la table
@Entity // Indique que cette classe correspond à une table dans la base de données
@Data // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor // creer un constructeur vide
@AllArgsConstructor // constructeur avec params
public class SessionPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionPlayerId;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private GameSession session;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter character;

    private Integer score;
    private Integer placement;
    private Boolean isWinner;
    private String notes;
}
