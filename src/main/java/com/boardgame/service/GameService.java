package com.boardgame.service;

import com.boardgame.model.dto.game.GameCreateDTO;
import com.boardgame.model.dto.game.GameDTO;
import com.boardgame.model.dto.game.GameUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.factory.GameFactory;
import com.boardgame.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Génère automatiquement un constructeur avec tous les champs 'final' → pratique pour l'injection via constructeur
@Transactional(readOnly = true) // Garantit que toutes les méthodes par défaut sont exécutées dans une transaction lecture seule
@Slf4j // Fournit un logger 'log' sans avoir à le déclarer manuellement
public class GameService {

    private final GameRepository gameRepository;
    private final GameFactory gameFactory;

    @Transactional
    public GameDTO createGame(GameCreateDTO dto) {
        log.debug("Saving game: {}", dto.name());
        Game game = gameFactory.fromCreateDto(dto);
        gameRepository.save(game);
        log.info("Game '{}' saved successfully", dto.name());
        return gameFactory.toDto(game);
    }

    public List<GameDTO> getAllGames() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .toList();
    }

    public GameDTO getGameById(Long id) {
        Game entity = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        return gameFactory.toDto(entity);
    }

    @Transactional
    public GameDTO updateGame(Long id, GameUpdateDTO dto) {
        Game entity = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        gameFactory.updateEntity(entity, dto);
        return gameFactory.toDto(gameRepository.save(entity));
    }

    @Transactional
    public void deleteGame(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found with id " + id);
        }
        gameRepository.deleteById(id);
    }
}
