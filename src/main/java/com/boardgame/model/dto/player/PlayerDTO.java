package com.boardgame.model.dto.player;

import com.boardgame.model.entity.SessionPlayer;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PlayerDTO (
    Long playerId,
    String playerName,
    String avatar,
    Integer gamesPlayed,
    Integer wins,
    Integer totalScore,
    Double averageScore,
    String favoriteGame,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<SessionPlayer> sessions)
        {}
