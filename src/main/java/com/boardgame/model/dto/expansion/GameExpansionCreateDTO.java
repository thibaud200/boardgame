package com.boardgame.model.dto.expansion;

import lombok.Builder;

@Builder
public record GameExpansionCreateDTO (
        Long gameId,
        Long bggExpansionId,
        String name,
        String description,
        Integer yearPublished)
{}
