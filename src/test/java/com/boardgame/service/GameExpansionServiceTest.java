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

@ExtendWith(MockitoExtension.class)
class GameExpansionServiceTest {

    @Mock
    private GameExpansionRepository gameExpansionRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameExpansionFactory gameExpansionFactory;

    @InjectMocks
    private GameExpansionService gameExpansionService;

    private GameExpansion gameExpansion;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setGameId(1L);
        game.setName("Terraforming Mars");

        gameExpansion = new GameExpansion();
        gameExpansion.setId(1L);
        gameExpansion.setBggExpansionId(12345L);
        gameExpansion.setName("Prélude");
        gameExpansion.setDescription("Extension de Terraforming Mars.");
        gameExpansion.setYearPublished(2017);
        gameExpansion.setGame(game);
    }

    @Test
    void shouldReturnAllExpansions() {
        when(gameExpansionRepository.findAll()).thenReturn(List.of(gameExpansion));

        GameExpansionDTO dto = GameExpansionDTO.builder()
                .id(1L)
                .gameId(1L)
                .bggExpansionId(12345L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .yearPublished(2017)
                .build();

        when(gameExpansionFactory.toDto(gameExpansion)).thenReturn(dto);

        List<GameExpansionDTO> result = gameExpansionService.getAllExpansions();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Prélude");
        verify(gameExpansionRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnExpansionById() {
        when(gameExpansionRepository.findById(1L)).thenReturn(Optional.of(gameExpansion));

        GameExpansionDTO dto = GameExpansionDTO.builder()
                .id(1L)
                .gameId(1L)
                .bggExpansionId(12345L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .yearPublished(2017)
                .build();

        when(gameExpansionFactory.toDto(gameExpansion)).thenReturn(dto);

        GameExpansionDTO result = gameExpansionService.getExpansionById(1L);

        assertThat(result.name()).isEqualTo("Prélude");
        verify(gameExpansionRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenExpansionNotFound() {
        when(gameExpansionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> gameExpansionService.getExpansionById(99L));

        verify(gameExpansionRepository, times(1)).findById(99L);
    }

    @Test
    void shouldCreateExpansion() {
        GameExpansionCreateDTO dto = GameExpansionCreateDTO.builder()
                .gameId(1L)
                .bggExpansionId(12345L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .yearPublished(2017)
                .build();

        GameExpansion newExpansion = new GameExpansion();
        newExpansion.setId(1L);
        newExpansion.setGame(game);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameExpansionFactory.fromCreateDto(dto, game)).thenReturn(newExpansion);
        when(gameExpansionRepository.save(any(GameExpansion.class))).thenReturn(newExpansion);

        GameExpansionDTO expansionDto = GameExpansionDTO.builder()
                .id(1L)
                .gameId(1L)
                .bggExpansionId(12345L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .yearPublished(2017)
                .build();

        when(gameExpansionFactory.toDto(any(GameExpansion.class))).thenReturn(expansionDto);

        GameExpansionDTO result = gameExpansionService.createExpansion(1L, dto);

        assertThat(result.name()).isEqualTo("Prélude");
        verify(gameRepository, times(1)).findById(1L);
        verify(gameExpansionRepository, times(1)).save(any(GameExpansion.class));
    }

    @Test
    void shouldUpdateExpansion() {
        GameExpansionUpdateDTO updateDto = GameExpansionUpdateDTO.builder()
                .name("Prélude Extended")
                .description("Nouvelle extension de Terraforming Mars.")
                .yearPublished(2019)
                .build();

        when(gameExpansionRepository.findById(1L)).thenReturn(Optional.of(gameExpansion));

        doAnswer(invocation -> {
            gameExpansion.setName("Prélude Extended");
            gameExpansion.setDescription("Nouvelle extension de Terraforming Mars.");
            gameExpansion.setYearPublished(2019);
            return null;
        }).when(gameExpansionFactory).updateEntity(gameExpansion, updateDto);

        when(gameExpansionRepository.save(gameExpansion)).thenReturn(gameExpansion);

        GameExpansionDTO expansionDto = GameExpansionDTO.builder()
                .id(1L)
                .gameId(1L)
                .bggExpansionId(12345L)
                .name("Prélude Extended")
                .description("Nouvelle extension de Terraforming Mars.")
                .yearPublished(2019)
                .build();

        when(gameExpansionFactory.toDto(gameExpansion)).thenReturn(expansionDto);

        GameExpansionDTO result = gameExpansionService.updateExpansion(1L, updateDto);

        assertThat(result.name()).isEqualTo("Prélude Extended");
        assertThat(result.yearPublished()).isEqualTo(2019);
    }

    @Test
    void shouldDeleteExpansion() {
        when(gameExpansionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gameExpansionRepository).deleteById(1L);

        gameExpansionService.deleteExpansion(1L);

        verify(gameExpansionRepository, times(1)).existsById(1L);
        verify(gameExpansionRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentExpansion() {
        when(gameExpansionRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> gameExpansionService.deleteExpansion(99L));

        verify(gameExpansionRepository, times(1)).existsById(99L);
        verify(gameExpansionRepository, never()).deleteById(any());
    }
}
