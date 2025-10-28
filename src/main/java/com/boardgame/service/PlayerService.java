package com.boardgame.service;

import com.boardgame.model.dto.player.PlayerCreateDTO;
import com.boardgame.model.dto.player.PlayerDTO;
import com.boardgame.model.dto.player.PlayerUpdateDTO;
import com.boardgame.model.entity.Player;
import com.boardgame.model.factory.PlayerFactory;
import com.boardgame.repository.PlayerRepository;
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
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerFactory playerFactory;

    @Transactional
    public PlayerDTO createPlayer(PlayerCreateDTO dto) {
        log.debug("Saving player: {}", dto.playerName());
        Player player = playerFactory.fromCreateDto(dto);
        playerRepository.save(player);
        log.info("Player '{}' saved successfully", dto.playerName());
        return playerFactory.toDto(player);
    }

    public List<PlayerDTO> getAllPLayers() {
        return playerRepository.findAll().stream()
                .map(playerFactory::toDto)
                .toList();
    }

    public PlayerDTO getPlayerById(Long id) {
        Player entity = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        return playerFactory.toDto(entity);
    }

    @Transactional
    public PlayerDTO updatePlayer(Long id, PlayerUpdateDTO dto) {
        Player entity = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found with id " + id));
        playerFactory.updateEntity(entity, dto);
        return playerFactory.toDto(playerRepository.save(entity));
    }

    @Transactional
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new EntityNotFoundException("Game not found with id " + id);
        }
        playerRepository.deleteById(id);
    }
}
