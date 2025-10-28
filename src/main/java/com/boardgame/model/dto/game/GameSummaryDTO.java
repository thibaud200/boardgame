package com.boardgame.model.dto.game;

import lombok.Builder;

@Builder
public class GameSummaryDTO {
    private Long gameId;
    private String name;
    private String category;
    private Double bggRating;
    private Integer yearPublished;
    private String image;
}
