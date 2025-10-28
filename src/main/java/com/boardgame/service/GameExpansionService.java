package com.boardgame.service;

import com.boardgame.model.dto.expansion.GameExpansionCreateDTO;
import com.boardgame.model.dto.expansion.GameExpansionDTO;
import com.boardgame.model.dto.expansion.GameExpansionUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.GameExpansion;
import com.boardgame.model.factory.GameExpansionFactory;
import com.boardgame.repository.GameExpansionRepository;
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
public class GameExpansionService {

    private final GameExpansionRepository gameExpansionRepository;
    private final GameRepository gameRepository;
    private final GameExpansionFactory gameExpansionFactory;

    @Transactional
    public GameExpansionDTO createExpansion(Long gameId, GameExpansionCreateDTO dto) {
        log.debug("Saving game: {}", dto.name());
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException(("Game not Found " + gameId)));
        GameExpansion gameExpansion = gameExpansionFactory.fromCreateDto(dto, game);
        gameExpansionRepository.save(gameExpansion);
        log.info("Game '{}' saved successfully", dto.name());
        return gameExpansionFactory.toDto(gameExpansion);
    }

    public List<GameExpansionDTO> getAllExpansions() {
        return gameExpansionRepository.findAll().stream()
                .map(gameExpansionFactory::toDto)
                .toList();
    }

    public GameExpansionDTO getExpansionById(Long id) {
        GameExpansion entity = gameExpansionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        return gameExpansionFactory.toDto(entity);
    }

    @Transactional
    public GameExpansionDTO updateExpansion(Long id, GameExpansionUpdateDTO dto) {
        GameExpansion entity = gameExpansionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        gameExpansionFactory.updateEntity(entity, dto);
        return gameExpansionFactory.toDto(gameExpansionRepository.save(entity));
    }

    @Transactional
    public void deleteExpansion(Long id) {
        if (!gameExpansionRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found with id " + id);
        }
        gameExpansionRepository.deleteById(id);
    }
}
