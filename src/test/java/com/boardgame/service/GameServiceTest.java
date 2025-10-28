package com.boardgame.service;

import com.boardgame.model.dto.game.GameCreateDTO;
import com.boardgame.model.dto.game.GameDTO;
import com.boardgame.model.dto.game.GameUpdateDTO;
import com.boardgame.model.entity.Game;
import com.boardgame.model.factory.GameFactory;
import com.boardgame.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // remplace AutoCloseable et permet de supprimer MockitoAnnotations.openMocks(this)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameFactory gameFactory;

    @InjectMocks
    private GameService gameService;

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setGameId(1L);
        game.setBggId(12345L);
        game.setName("Terraforming Mars");
        game.setDescription("Colonise Mars et fais prospérer ton entreprise.");
        game.setMinPlayers(1);
        game.setMaxPlayers(5);
    }

    @Test
    void shouldReturnAllGames() {
        when(gameRepository.findAll()).thenReturn(List.of(game));
        GameDTO gameDto = GameDTO.builder()
                .gameId(1L)
                .bggId(12345L)
                .name("Terraforming Mars")
                .description("Description")
                .minPlayers(1)
                .maxPlayers(5)
                .build();
        when(gameFactory.toDto(game)).thenReturn(gameDto);

        List<GameDTO> result = gameService.getAllGames();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Terraforming Mars");
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnGameById() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        GameDTO gameDto = GameDTO.builder()
                .gameId(1L)
                .bggId(12345L)
                .name("Terraforming Mars")
                .description("Description")
                .minPlayers(1)
                .maxPlayers(5)
                .build();
        when(gameFactory.toDto(game)).thenReturn(gameDto);

        GameDTO dto = gameService.getGameById(1L);

        assertThat(dto.name()).isEqualTo("Terraforming Mars");
        verify(gameRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> gameService.getGameById(99L)
        );
    }

    @Test
    void shouldCreateGame() {
        GameCreateDTO gameCreateDto = GameCreateDTO.builder()
                .bggId(12345L)
                .name("Terraforming Mars")
                .description("Description")
                .minPlayers(1)
                .maxPlayers(5)
                .build();

        Game newGame = new Game();
        newGame.setGameId(1L);

        when(gameFactory.fromCreateDto(gameCreateDto)).thenReturn(newGame);
        when(gameRepository.save(any(Game.class))).thenReturn(newGame);
        GameDTO gameDto = GameDTO.builder()
                .bggId(12345L)
                .name("Terraforming Mars")
                .description("Description")
                .minPlayers(1)
                .maxPlayers(5)
                .build();
        when(gameFactory.toDto(any(Game.class))).thenReturn(gameDto);

        GameDTO result = gameService.createGame(gameCreateDto);

        assertThat(result.name()).isEqualTo("Terraforming Mars");
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void shouldUpdateGame() {
        GameUpdateDTO dto = GameUpdateDTO.builder()
                .bggId(12345L)
                .name("Terraforming Mars Updated")
                .description("Colonise Mars et fais prospérer ton entreprise.")
                .minPlayers(2)
                .maxPlayers(6)
                .build();

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        doAnswer(invocation -> {
            game.setName("Terraforming Mars Updated");
            game.setDescription("Nouvelle description");
            game.setMinPlayers(2);
            game.setMaxPlayers(6);
            return null;
        }).when(gameFactory).updateEntity(game, dto);

        when(gameRepository.save(game)).thenReturn(game);
        GameDTO gameDto = GameDTO.builder()
                .gameId(1L)
                .bggId(12345L)
                .name("Terraforming Mars Updated")
                .description("Colonise Mars et fais prospérer ton entreprise.")
                .minPlayers(2)
                .maxPlayers(6)
                .build();
        when(gameFactory.toDto(game)).thenReturn(gameDto);

        GameDTO result = gameService.updateGame(1L, dto);

        assertThat(result.name()).isEqualTo("Terraforming Mars Updated");
        assertThat(result.minPlayers()).isEqualTo(2);
        assertThat(result.maxPlayers()).isEqualTo(6);
    }

    @Test
    void shouldDeleteGame() {
        when(gameRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gameRepository).deleteById(1L);

        gameService.deleteGame(1L);

        verify(gameRepository, times(1)).existsById(1L);
        verify(gameRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentGame() {
        when(gameRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> gameService.deleteGame(99L)
        );

        verify(gameRepository, times(1)).existsById(99L);
        verify(gameRepository, never()).deleteById(any());
    }
}
