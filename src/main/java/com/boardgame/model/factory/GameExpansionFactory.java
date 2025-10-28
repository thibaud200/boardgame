package com.boardgame.model.factory;

import com.boardgame.model.dto.expansion.GameExpansionCreateDTO;
import com.boardgame.model.dto.expansion.GameExpansionDTO;
import com.boardgame.model.dto.expansion.GameExpansionUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameExpansion;
import org.springframework.stereotype.Component;

@Component
public class GameExpansionFactory {
    public GameExpansionDTO toDto(GameExpansion entity) {
        return new GameExpansionDTO(
                entity.getId(),
                entity.getGame().getGameId(),
                entity.getBggExpansionId(),
                entity.getName(),
                entity.getDescription(),
                entity.getYearPublished(),
                entity.getUpdatedAt(),
                entity.getCreatedAt()
                );
    }

    public GameExpansion fromCreateDto(GameExpansionCreateDTO dto, Game game) {
        GameExpansion gameExpansion = new GameExpansion();
        gameExpansion.setName(dto.name());
        gameExpansion.setDescription(dto.description());
        gameExpansion.setGame(game);
        gameExpansion.setBggExpansionId(dto.bggExpansionId());
        gameExpansion.setYearPublished(dto.yearPublished());
        return gameExpansion;
    }

    public void updateEntity(GameExpansion gameExpansion, GameExpansionUpdateDTO dto) {
        if (dto.description() != null) gameExpansion.setDescription(dto.description());
        if (dto.id() != null) gameExpansion.setId(dto.id());
        if (dto.bggExpansionId() != null) gameExpansion.setBggExpansionId(dto.bggExpansionId());
        if (dto.name() != null) gameExpansion.setName(dto.name());
        if (dto.yearPublished() != null) gameExpansion.setYearPublished(dto.yearPublished());
    }
}
