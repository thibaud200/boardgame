package com.boardgame.model.factory;

import com.boardgame.model.dto.player.PlayerCreateDTO;
import com.boardgame.model.dto.player.PlayerDTO;
import com.boardgame.model.dto.player.PlayerUpdateDTO;
import com.boardgame.model.entity.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {

    public PlayerDTO toDto(Player entity) {
        return new PlayerDTO(
                entity.getPlayerId(),
                entity.getPlayerName(),
                entity.getAvatar(),
                entity.getGamesPlayed(),
                entity.getWins(),
                entity.getTotalScore(),
                entity.getAverageScore(),
                entity.getFavoriteGame(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getSessions()
                );
    }

    public Player fromCreateDto(PlayerCreateDTO dto) {
        Player player = new Player();
        player.setPlayerName(dto.playerName());
        player.setAvatar(dto.avatar());
        player.setWins(dto.wins());
        player.setAverageScore(dto.averageScore());
        player.setGamesPlayed(dto.gamesPlayed());
        player.setTotalScore(dto.totalScore());
        player.setFavoriteGame(dto.favoriteGame());
        return player;
    }

    public void updateEntity(Player player, PlayerUpdateDTO dto) {
        if (dto.playerName() != null) player.setPlayerName(dto.playerName());
        if (dto.avatar() != null) player.setAvatar(dto.avatar());
        if (dto.wins() != null) player.setWins(dto.wins());
        if (dto.averageScore() != null) player.setAverageScore(dto.averageScore());
        if (dto.gamesPlayed() != null) player.setGamesPlayed(dto.gamesPlayed());
        if (dto.totalScore() != null) player.setTotalScore(dto.totalScore());
        if (dto.favoriteGame() != null) player.setFavoriteGame(dto.favoriteGame());
    }
}
