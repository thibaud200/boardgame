package com.boardgame.controller;

import com.boardgame.model.dto.player.PlayerCreateDTO;
import com.boardgame.model.dto.player.PlayerDTO;
import com.boardgame.model.dto.player.PlayerUpdateDTO;
import com.boardgame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor // Injecte automatiquement PlayerService sans avoir besoin de @Autowired

public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/{id}")
    public PlayerDTO getById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @GetMapping
    public List<PlayerDTO> getAll() {
        return playerService.getAllPLayers();
    }

    @PostMapping
    public ResponseEntity<PlayerDTO> create(@RequestBody PlayerCreateDTO dto) {
        PlayerDTO created = playerService.createPlayer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public PlayerDTO update(@PathVariable Long id, @RequestBody PlayerUpdateDTO dto) {
        return playerService.updatePlayer(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
