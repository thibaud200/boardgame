package com.boardgame.controller;

import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameCharacter;
import com.boardgame.repository.GameCharacterRepository;
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

@SpringBootTest //declaration pour les test via springboot
@AutoConfigureMockMvc //configuration pour le mockMvc
class GameCharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameCharacterRepository characterRepository;

    private Long characterId;
    private Long gameId;

    @BeforeEach
    void setup() {
        characterRepository.deleteAll();
        gameRepository.deleteAll();

        Game game = new Game();
        game.setName("Gloomhaven");
        game.setBggId(98765L);
        game.setMinPlayers(1);
        game.setMaxPlayers(4);
        Game savedGame = gameRepository.saveAndFlush(game);
        gameId = savedGame.getGameId();

        GameCharacter character = new GameCharacter();
        character.setName("Brute");
        character.setKey("brute");
        character.setDescription("Strong warrior");
        character.setGame(savedGame);
        GameCharacter savedCharacter = characterRepository.save(character);
        this.characterId = savedCharacter.getId();
    }

    @Test
    void shouldReturnAllCharacters() throws Exception {
        mockMvc.perform(get("/api/characters")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Brute"));
    }

    @Test
    void shouldReturnCharacterById() throws Exception {
        mockMvc.perform(get("/api/characters/" + characterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brute"))
                .andExpect(jsonPath("$.key").value("brute"));
    }

    @Test
    void shouldCreateCharacter() throws Exception {
        String json = """
                {
                  "gameId": %d,
                  "key": "mindthief",
                  "name": "Mindthief",
                  "description": "Mental powers"
                }
                """.formatted(gameId);

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Mindthief"));
    }

    @Test
    void shouldUpdateCharacter() throws Exception {
        String json = """
                {
                  "name": "Brute Updated",
                  "description": "Updated description"
                }
                """;

        mockMvc.perform(put("/api/characters/" + characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brute Updated"));
    }

    @Test
    void shouldDeleteCharacter() throws Exception {
        mockMvc.perform(delete("/api/characters/" + characterId))
                .andExpect(status().isNoContent());
    }
}
