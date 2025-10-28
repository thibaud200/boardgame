package com.boardgame.controller;

import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameExpansion;
import com.boardgame.repository.GameExpansionRepository;
import com.boardgame.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GameExpansionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameExpansionRepository expansionRepository;

    private Long expansionId;
    private Long gameId;

    @BeforeEach
    void setup() {
        expansionRepository.deleteAll();
        gameRepository.deleteAll();

        Game game = new Game();
        game.setName("Terraforming Mars");
        game.setBggId(12345L);
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
        Game savedGame = gameRepository.saveAndFlush(game);
        gameId = savedGame.getGameId();

        GameExpansion expansion = new GameExpansion();
        expansion.setGame(savedGame);
        expansion.setName("Prelude");
        expansion.setDescription("Early game boost");
        expansionRepository.save(expansion);
        this.expansionId = expansion.getId();
    }

    @Test
    void shouldReturnAllExpansions() throws Exception {
        mockMvc.perform(get("/api/expansions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Prelude"));
    }

    @Test
    void shouldReturnExpansionById() throws Exception {
        mockMvc.perform(get("/api/expansions/" + expansionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prelude"));
    }

    @Test
    void shouldCreateExpansion() throws Exception {
        String json = """
                {
                  "gameId": %d,
                  "bggExpansionId": 54321,
                  "name": "Venus Next",
                  "description": "Adds new projects and corporations",
                  "yearPublished": 2017
                }
                """.formatted(gameId);

        mockMvc.perform(post("/api/expansions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Venus Next"));
    }

    @Test
    void shouldUpdateExpansion() throws Exception {
        String json = """
                {
                  "name": "Prelude Updated",
                  "description": "Improved early game"
                }
                """;

        mockMvc.perform(put("/api/expansions/" + expansionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Prelude Updated"));
    }

    @Test
    void shouldDeleteExpansion() throws Exception {
        mockMvc.perform(delete("/api/expansions/" + expansionId))
                .andExpect(status().isNoContent());
    }
}
