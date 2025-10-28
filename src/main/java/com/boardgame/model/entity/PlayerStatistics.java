package com.boardgame.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable // empêche la persistance et les mises à jour via Hibernate (lecture seule)
@Subselect("SELECT * FROM player_statistics")
public class PlayerStatistics {
    @Id
    private Long playerId;
    private String playerName;
    private Double winPercentage;
    // ...
}
