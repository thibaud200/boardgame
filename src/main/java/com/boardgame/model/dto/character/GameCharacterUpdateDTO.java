package com.boardgame.model.dto.character;

import lombok.Builder;

import java.util.List;

@Builder
public record GameCharacterUpdateDTO  (
        Long id,
        Long gameId,             // jeu associé
        String key,             // identifiant unique
        String name,
        String description,
        String avatar,
        List<String> abilities,
        String gameName)
{}
