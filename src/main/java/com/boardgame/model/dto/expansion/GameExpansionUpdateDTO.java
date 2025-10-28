package com.boardgame.model.dto.expansion;

import lombok.Builder;

@Builder
public record GameExpansionUpdateDTO (
        Long id,
        Long gameId,
        Long bggExpansionId,
        String name,
        String description,
        Integer yearPublished)
{}
