package com.boardgame.model.dto.expansion;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GameExpansionDTO (
    Long id,
    Long gameId,
    Long bggExpansionId,
    String name,
    String description,
    Integer yearPublished,
    LocalDateTime createdAt,
    LocalDateTime updatedAt)
{}
