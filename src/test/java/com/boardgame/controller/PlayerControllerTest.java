package com.boardgame.controller;

import com.boardgame.model.entity.Player;
import com.boardgame.repository.PlayerRepository;
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
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    private Long playerId;

    @BeforeEach
    void setup() {
        playerRepository.deleteAll();

        Player player = new Player();
        player.setPlayerName("Thibs");
        player.setFavoriteGame("Pandemie");
        player.setAvatar("");
        Player saved = playerRepository.save(player);
        this.playerId = saved.getPlayerId();
    }

    @Test
    void shouldReturnAllPlayers() throws Exception {
        mockMvc.perform(get("/api/players")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerName").value("Thibs"));
    }

    @Test
    void shouldReturnPlayerById() throws Exception {
        mockMvc.perform(get("/api/players/" + playerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("Thibs"))
                .andExpect(jsonPath("$.favoriteGame").value("Pandemie"));
    }

    @Test
    void shouldCreatePlayer() throws Exception {
        String json = """
                {
                    "playerName": "Nova",
                    "favoriteGame": "Catan",
                    "avatar": "azerty"
                }
                """;

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerName").value("Nova"))
                .andExpect(jsonPath("$.favoriteGame").value("Catan"));
    }

    @Test
    void shouldUpdatePlayer() throws Exception {
        String json = """
                {
                    "playerName": "ThibsUpdated",
                    "favoriteGame": "Catan"
                }
                """;

        mockMvc.perform(put("/api/players/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerName").value("ThibsUpdated"))
                .andExpect(jsonPath("$.favoriteGame").value("Catan"));
    }

    @Test
    void shouldDeletePlayer() throws Exception {
        mockMvc.perform(delete("/api/players/" + playerId))
                .andExpect(status().isNoContent());
    }
}

