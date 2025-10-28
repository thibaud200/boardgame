package com.boardgame.model.dto.game;

import lombok.Builder;

@Builder
public record GameCreateDTO (
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
    Boolean hasCharacters)
{}
