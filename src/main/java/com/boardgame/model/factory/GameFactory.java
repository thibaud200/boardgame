package com.boardgame.model.factory;

import com.boardgame.model.dto.character.GameCharacterDTO;
import com.boardgame.model.dto.expansion.GameExpansionDTO;
import com.boardgame.model.dto.game.GameCreateDTO;
import com.boardgame.model.dto.game.GameDTO;
import com.boardgame.model.dto.game.GameUpdateDTO;
import com.boardgame.model.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameFactory {

    private final GameExpansionFactory expansionFactory;
    private final GameCharacterFactory characterFactory;

    public GameDTO toDto(Game entity) {
        // Transformation des listes d'entités en listes de DTOs
        List<GameExpansionDTO> expansions = entity.getExpansions() == null
                ? Collections.emptyList()
                : entity.getExpansions().stream()
                .map(expansionFactory::toDto)
                .toList();

        List<GameCharacterDTO> characters = entity.getCharacters() == null
                ? Collections.emptyList()
                : entity.getCharacters().stream()
                .map(characterFactory::toDto)
                .toList();
        return new GameDTO(
                entity.getGameId(),
                entity.getBggId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImage(),
                entity.getMinPlayers(),
                entity.getMaxPlayers(),
                entity.getDuration(),
                entity.getDifficulty(),
                entity.getCategory(),
                entity.getYearPublished(),
                entity.getPublisher(),
                entity.getDesigner(),
                entity.getBggRating(),
                entity.getWeight(),
                entity.getAgeMin(),
                entity.getGameType(),
                entity.getSupportsCooperative(),
                entity.getSupportsCompetitive(),
                entity.getSupportsCampaign(),
                entity.getSupportsHybrid(),
                entity.getHasExpansion(),
                entity.getHasCharacters(),
                expansions,
                characters,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
                );
    }

    public Game fromCreateDto(GameCreateDTO dto) {
        Game game = new Game();
        game.setBggId(dto.bggId());
        game.setName(dto.name());
        game.setDescription(dto.description());
        game.setImage(dto.image());
        game.setMinPlayers(dto.minPlayers());
        game.setMaxPlayers(dto.maxPlayers());
        game.setDuration(dto.duration());
        game.setDifficulty(dto.difficulty());
        game.setCategory(dto.category());
        game.setYearPublished(dto.yearPublished());
        game.setPublisher(dto.publisher());
        game.setDesigner(dto.designer());
        game.setBggRating(dto.bggRating());
        game.setWeight(dto.weight());
        game.setAgeMin(dto.ageMin());
        game.setGameType(dto.gameType());
        game.setSupportsCooperative(dto.supportsCooperative());
        game.setSupportsCompetitive(dto.supportsCompetitive());
        game.setSupportsCampaign(dto.supportsCampaign());
        game.setSupportsHybrid(dto.supportsHybrid());
        game.setHasExpansion(dto.hasExpansion());
        game.setHasCharacters(dto.hasCharacters());
        return game;
    }

    public void updateEntity(Game game, GameUpdateDTO dto) {
        if (dto.bggId() != null) game.setBggId(dto.bggId());
        if (dto.name() != null) game.setName(dto.name());
        if (dto.description() != null) game.setDescription(dto.description());
        if (dto.image() != null) game.setImage(dto.image());
        if (dto.minPlayers() > 0) game.setMinPlayers(dto.minPlayers());
        if (dto.maxPlayers() > 0) game.setMaxPlayers(dto.maxPlayers());
        if (dto.duration() != null) game.setDuration(dto.duration());
        if (dto.difficulty() != null) game.setDifficulty(dto.difficulty());
        if (dto.category() != null) game.setCategory(dto.category());
        if (dto.yearPublished() != null) game.setYearPublished(dto.yearPublished());
        if (dto.publisher() != null) game.setPublisher(dto.publisher());
        if (dto.designer() != null) game.setDesigner(dto.designer());
        if (dto.bggRating() != null) game.setBggRating(dto.bggRating());
        if (dto.weight() != null) game.setWeight(dto.weight());
        if (dto.ageMin() != null) game.setAgeMin(dto.ageMin());
        if (dto.gameType() != null) game.setGameType(dto.gameType());
        if (dto.supportsCooperative() != null) game.setSupportsCooperative(dto.supportsCooperative());
        if (dto.supportsCompetitive() != null) game.setSupportsCompetitive(dto.supportsCompetitive());
        if (dto.supportsCampaign() != null) game.setSupportsCampaign(dto.supportsCampaign());
        if (dto.supportsHybrid() != null) game.setSupportsHybrid(dto.supportsHybrid());
        if (dto.hasExpansion() != null) game.setHasExpansion(dto.hasExpansion());
        if (dto.hasCharacters() != null) game.setHasCharacters(dto.hasCharacters());
    }
}
