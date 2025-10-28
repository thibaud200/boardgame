package com.boardgame.model.dto.game;

import java.time.LocalDateTime;
import java.util.List;
import com.boardgame.model.dto.character.GameCharacterDTO;
import com.boardgame.model.dto.expansion.GameExpansionDTO;
import lombok.Builder;

@Builder
public record GameDTO (
    Long gameId,
    Long bggId,
    String name,
    String description,
    String image,
    int minPlayers,
    int maxPlayers,
    String duration,
    String difficulty,
    String category,
    Integer yearPublished,
    String publisher,
    String designer,
    Double bggRating,
    Double weight,
    Integer ageMin,
    String gameType,
    Boolean supportsCooperative,
    Boolean supportsCompetitive,
    Boolean supportsCampaign,
    Boolean supportsHybrid,
    Boolean hasExpansion,
    Boolean hasCharacters,
    List<GameExpansionDTO> expansions,
    List<GameCharacterDTO> characters,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
{}
