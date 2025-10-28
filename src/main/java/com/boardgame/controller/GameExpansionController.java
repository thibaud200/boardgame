package com.boardgame.controller;

import com.boardgame.model.dto.expansion.GameExpansionCreateDTO;
import com.boardgame.model.dto.expansion.GameExpansionDTO;
import com.boardgame.model.dto.expansion.GameExpansionUpdateDTO;
import com.boardgame.service.GameExpansionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/expansions")
@RequiredArgsConstructor // Injecte automatiquement GameExpansionService sans avoir besoin de @Autowired

public class GameExpansionController {

    private final GameExpansionService gameExpansionService;

    @GetMapping("/{id}")
    public GameExpansionDTO getById(@PathVariable Long id) {
        return gameExpansionService.getExpansionById(id);
    }

    @GetMapping
    public List<GameExpansionDTO> getAll() {
        return gameExpansionService.getAllExpansions();
    }

    @PostMapping
    public ResponseEntity<GameExpansionDTO> create(@RequestBody GameExpansionCreateDTO dto) {
        GameExpansionDTO created = gameExpansionService.createExpansion(dto.gameId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public GameExpansionDTO update(@PathVariable Long id, @RequestBody GameExpansionUpdateDTO dto) {
        return gameExpansionService.updateExpansion(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameExpansionService.deleteExpansion(id);
        return ResponseEntity.noContent().build();
    }
}
