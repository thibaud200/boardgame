package com.boardgame.model.factory;

import com.boardgame.model.dto.character.*;
import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameCharacter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GameCharacterFactory {

    private static final ObjectMapper mapper = new ObjectMapper();

    public GameCharacterDTO toDto(GameCharacter entity) {
        List<String> abilities = parseAbilities(entity.getAbilities());
        return new GameCharacterDTO(
                entity.getId(),
                entity.getGame() != null ? entity.getGame().getGameId() : null,
                entity.getKey(),
                entity.getName(),
                entity.getDescription(),
                entity.getAvatar(),
                abilities,
                entity.getGame() != null ? entity.getGame().getName() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
                );
    }

    public GameCharacter fromCreateDto(GameCharacterCreateDTO dto, Game game) {
        GameCharacter entity = new GameCharacter();
        entity.setGame(game);
        entity.setKey(dto.key());
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setAvatar(dto.avatar());
        entity.setAbilities(toJson(dto.abilities()));
        return entity;
    }

    public void updateEntity(GameCharacter gameCharacter, GameCharacterUpdateDTO dto) {
        if (dto.description() != null) gameCharacter.setDescription(dto.description());
        if (dto.name() != null) gameCharacter.setName(dto.name());
        if (dto.description() != null) gameCharacter.setDescription(dto.description());
        if (dto.avatar() != null) gameCharacter.setAvatar(dto.avatar());
        if (dto.abilities() != null) gameCharacter.setAbilities(toJson(dto.abilities()));
    }

    private static String toJson(List<String> abilities) {
        try {
            return mapper.writeValueAsString(abilities);
        } catch (Exception e) {
            return "[]";
        }
    }

    private static List<String> parseAbilities(String json) {
        try {
            return mapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
