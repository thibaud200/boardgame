package com.boardgame.controller;

import com.boardgame.model.entity.Game;
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

@SpringBootTest // charge tout le contexte Spring pour le test
@AutoConfigureMockMvc // active MockMvc (simulation HTTP) pour le test
class GameControllerTest {

    private Long gameId;
    private Long gameIdUpdate;

    @Autowired // importe le constructeur
    private MockMvc mockMvc;

    @Autowired // importe le constructeur
    private GameRepository gameRepository;

    @BeforeEach // boucle sur chaque élément pour déclarer un test
    void setup() {
        gameRepository.deleteAll();
        Game game = new Game();
        game.setBggId(12345L);
        game.setName("Terraforming Mars");
        game.setDescription("Colonise Mars et fais prospérer ton entreprise.");
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
        Game saved = gameRepository.save(game);
        this.gameId = saved.getGameId(); // 🔹 stocke l’ID du jeu sauvegardé
        Game game2 = new Game();
        game2.setBggId(67890L);
        game2.setName("Catan");
        game2.setDescription("Construis, échange et colonise des îles !");
        game2.setMinPlayers(3);
        game2.setMaxPlayers(4);
        Game update = gameRepository.save(game2);
        this.gameIdUpdate = update.getGameId();
    }

    @Test //définit une méthode de test
    void shouldReturnGameById() throws Exception {
        mockMvc.perform(get("/api/games/" + gameId)// 🔹 utilise l’ID réel
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Terraforming Mars"))
                .andExpect(jsonPath("$.minPlayers").value(1))
                .andExpect(jsonPath("$.maxPlayers").value(5));
    }

    @Test //définit une méthode de test
    void shouldReturnGames() throws Exception {
        mockMvc.perform(get("/api/games")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // andExpect == assert
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Terraforming Mars"))
                .andExpect(jsonPath("$[1].name").value("Catan"));
    }

    @Test
    void shouldCreateGame() throws Exception {
        String json = """
                {
                  "bggId": 67891,
                  "name": "Les Demeures de l'épouvante",
                  "description": "Jeu de coopération.",
                  "minPlayers": 3,
                  "maxPlayers": 5
                }
                """;

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Les Demeures de l'épouvante"))
                .andExpect(jsonPath("$.minPlayers").value(3))
                .andExpect(jsonPath("$.maxPlayers").value(5));
    }

    @Test
    void shouldUpdateGame() throws Exception {
        String json = """
                {
                  "bggId": 67890,
                  "name": "Catan",
                  "description": "Construis, échange et colonise des îles !",
                  "minPlayers": 3,
                  "maxPlayers": 5
                }
                """;

        mockMvc.perform(put("/api/games/" + gameIdUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Catan"))
                .andExpect(jsonPath("$.maxPlayers").value(5));
    }

    @Test
    void shouldDeleteGame() throws Exception {
        mockMvc.perform(delete("/api/games/" + gameId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentGame() throws Exception {
        mockMvc.perform(delete("/api/games/99999"))
                .andExpect(status().isNotFound());
    }
}

