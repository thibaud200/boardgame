package com.boardgame.service;

import com.boardgame.model.dto.character.GameCharacterCreateDTO;
import com.boardgame.model.dto.character.GameCharacterDTO;
import com.boardgame.model.dto.character.GameCharacterUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameCharacter;
import com.boardgame.model.factory.GameCharacterFactory;
import com.boardgame.repository.GameCharacterRepository;
import com.boardgame.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // Génère automatiquement un constructeur avec tous les champs 'final' → pratique pour l'injection via constructeur
@Transactional(readOnly = true) // Garantit que toutes les méthodes par défaut sont exécutées dans une transaction lecture seule
@Slf4j // Fournit un logger 'log' sans avoir à le déclarer manuellement
public class GameCharacterService {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameRepository gameRepository;
    private final GameCharacterFactory gameCharacterFactory;

    @Transactional
    public GameCharacterDTO createCharacter(Long gameId, GameCharacterCreateDTO dto) {
        log.debug("Saving game: {}", dto.name());
        // Vérifie que le jeu existe
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + gameId));
        GameCharacter gameCharacter = gameCharacterFactory.fromCreateDto(dto, game);
        gameCharacterRepository.save(gameCharacter);
        log.info("Game '{}' saved successfully", dto.name());
        return gameCharacterFactory.toDto(gameCharacter);
    }

    public List<GameCharacterDTO> getAllCharacters() {
        return gameCharacterRepository.findAll().stream()
                .map(gameCharacterFactory::toDto)
                .toList();
    }

    public GameCharacterDTO getCharacterById(Long id) {
        GameCharacter entity = gameCharacterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        return gameCharacterFactory.toDto(entity);
    }

    @Transactional
    public GameCharacterDTO updateCharacter(Long id, GameCharacterUpdateDTO dto) {
        GameCharacter entity = gameCharacterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        gameCharacterFactory.updateEntity(entity, dto);
        return gameCharacterFactory.toDto(gameCharacterRepository.save(entity));
    }

    @Transactional
    public void deleteCharacter(Long id) {
        if (!gameCharacterRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found with id " + id);
        }
        gameCharacterRepository.deleteById(id);
    }
}
