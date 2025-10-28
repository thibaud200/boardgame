package com.boardgame.controller;

import com.boardgame.model.dto.game.GameCreateDTO;
import com.boardgame.model.dto.game.GameDTO;
import com.boardgame.model.dto.game.GameUpdateDTO;
import com.boardgame.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor // Injecte automatiquement GameService sans avoir besoin de @Autowired

public class GameController {

    private final GameService gameService;

    @GetMapping("/{id}")
    public GameDTO getById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @GetMapping
    public List<GameDTO> getAll() {
        return gameService.getAllGames();
    }

    @PostMapping
    public ResponseEntity<GameDTO> create(@RequestBody GameCreateDTO dto) {
        GameDTO created = gameService.createGame(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public GameDTO update(@PathVariable Long id, @RequestBody GameUpdateDTO dto) {
        return gameService.updateGame(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
