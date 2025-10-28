package com.boardgame.controller;

import com.boardgame.model.dto.character.GameCharacterCreateDTO;
import com.boardgame.model.dto.character.GameCharacterDTO;
import com.boardgame.model.dto.character.GameCharacterUpdateDTO;
import com.boardgame.service.GameCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor // Injecte automatiquement GameExpansionService sans avoir besoin de @Autowired

public class GameCharacterController {

    private final GameCharacterService gameCharacterService;

    @GetMapping("/{id}")
    public GameCharacterDTO getById(@PathVariable Long id) {
        return gameCharacterService.getCharacterById(id);
    }

    @GetMapping
    public List<GameCharacterDTO> getAll() {
        return gameCharacterService.getAllCharacters();
    }

    @PostMapping
    public ResponseEntity<GameCharacterDTO> create(@RequestBody GameCharacterCreateDTO dto) {
        GameCharacterDTO created = gameCharacterService.createCharacter(dto.gameId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public GameCharacterDTO update(@PathVariable Long id, @RequestBody GameCharacterUpdateDTO dto) {
        return gameCharacterService.updateCharacter(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gameCharacterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
