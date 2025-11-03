package com.boardgame.service;

import com.boardgame.model.dto.game.GameCreateDTO;
import com.boardgame.model.dto.game.GameDTO;
import com.boardgame.model.dto.game.GameUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.factory.GameFactory;
import com.boardgame.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    //EXO 1
    public List<String> getAllGamesByNameSorted() {
        return gameRepository.findAll().stream()
                //trier par ordre insensible a la casse
                .map(gameFactory::toDto).map(GameDTO::name).sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    //EXO 2
    public List<GameDTO> getAllGamesByNote(Double bggRating) {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .filter(game -> game.bggRating()>=bggRating)
                .sorted(Comparator.comparingDouble(GameDTO::bggRating).reversed())
                .toList();
    }

    //EXO 3
    public Double getAllGamesByAvgNote() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .mapToDouble(GameDTO::bggRating).average().orElse(0);
    }

    //EXO 4
    public Map<String, List<GameDTO>> getAllGamesByGenre() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .collect(Collectors.groupingBy(
                        GameDTO::gameType,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(GameDTO::name))
                                        .toList()
                        )
                ));
        //      Map<String, List<GameDTO>> grouped = gameRepository.findAll().stream()
        //                .map(gameFactory::toDto)
        //                .collect(Collectors.groupingBy(GameDTO::gameType));
        //
        //        grouped.forEach(
        //                (genre, games) -> games.sort(Comparator.comparing(GameDTO::name))
        //        );
        //        return grouped;
    }

    //EXO 5
    public Map<String, Double> getAllGamesAvgByGenre() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .collect(Collectors.groupingBy(
                        GameDTO::gameType,
                        Collectors.averagingDouble(GameDTO::bggRating)
                        )
                );
    }

    //EXO 6
    public String getAllGamesCsvSorted() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .map(GameDTO::name)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    //EXO 7
    public Optional<GameDTO> getAllGamesMostAncien() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .min(Comparator.comparing(GameDTO::yearPublished
                        //pour tester si la date est null afin de ne pas lever dexception
                        , Comparator.nullsLast(Comparator.naturalOrder())));
    }

    //EXO 8
    public List<GameDTO> getAllGamesByGenreWithRate(String type, Double rating) {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .filter(typeGame -> type.equals(typeGame.gameType()))
                .filter(game -> game.bggRating()>=rating)
                .sorted(Comparator.comparing(GameDTO::name))
                .toList();
    }

    //EXO 9
    public List<String> getAllGamesByGenreSorted() {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                .map(GameDTO::gameType).distinct().sorted().toList();
    }

    //EXO 10
    public Map<Boolean, List<GameDTO>> getAllGamesByYearPartition(Double minYear, Double maxYear) {
        return gameRepository.findAll().stream()
                .map(gameFactory::toDto)
                //.filter(yearMin -> yearMin.yearPublished()>=minYear)
                //.filter(yearMax -> yearMax.yearPublished()<=maxYear)
                .collect(Collectors.partitioningBy(g -> g.yearPublished() >= minYear
                        && g.yearPublished() <= maxYear));
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
