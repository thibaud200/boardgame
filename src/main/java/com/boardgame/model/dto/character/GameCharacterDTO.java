package com.boardgame.model.dto.character;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GameCharacterDTO (
    Long id,
    Long gameId,             // jeu associé
    String key,             // identifiant unique
    String name,
    String description,
    String avatar,
    List<String> abilities,
    String gameName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
{}
