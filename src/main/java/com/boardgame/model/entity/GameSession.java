package com.boardgame.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "game_sessions")
@Entity // Indique que cette classe correspond à une table dans la base de données
@Data // Génère getters, setters, equals, hashCode, toString
@NoArgsConstructor // creer un constructeur vide
@AllArgsConstructor // constructeur avec params
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String sessionType;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "winner_player_id")
    private Player winner;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<SessionPlayer> participants;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
