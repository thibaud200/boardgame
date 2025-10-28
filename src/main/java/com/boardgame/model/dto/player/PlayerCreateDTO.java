package com.boardgame.model.dto.player;

import com.boardgame.model.entity.SessionPlayer;
import lombok.Builder;

import java.util.List;

@Builder
public record PlayerCreateDTO  (
        String playerName,
        String avatar,
        Integer gamesPlayed,
        Integer wins,
        Integer totalScore,
        Double averageScore,
        String favoriteGame,
        List<SessionPlayer> sessions)
{}
