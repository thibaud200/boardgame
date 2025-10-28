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
class GameCharacterServiceTest {

    @Mock
    private GameCharacterRepository gameCharacterRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameCharacterFactory gameCharacterFactory;

    @InjectMocks
    private GameCharacterService gameCharacterService;

    private GameCharacter gameCharacter;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setGameId(1L);
        game.setName("Citadels");

        gameCharacter = new GameCharacter();
        gameCharacter.setId(1L);
        gameCharacter.setName("test");
        gameCharacter.setDescription("Jeu avec des role");
        gameCharacter.setGame(game);
    }

    @Test
    void shouldReturnAllCharacters() {
        when(gameCharacterRepository.findAll()).thenReturn(List.of(gameCharacter));

        GameCharacterDTO dto = GameCharacterDTO.builder()
                .id(1L)
                .gameId(1L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .build();

        when(gameCharacterFactory.toDto(gameCharacter)).thenReturn(dto);

        List<GameCharacterDTO> result = gameCharacterService.getAllCharacters();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Prélude");
        verify(gameCharacterRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnCharacterById() {
        when(gameCharacterRepository.findById(1L)).thenReturn(Optional.of(gameCharacter));

        GameCharacterDTO dto = GameCharacterDTO.builder()
                .id(1L)
                .gameId(1L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .build();

        when(gameCharacterFactory.toDto(gameCharacter)).thenReturn(dto);

        GameCharacterDTO result = gameCharacterService.getCharacterById(1L);

        assertThat(result.name()).isEqualTo("Prélude");
        verify(gameCharacterRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenCharacterNotFound() {
        when(gameCharacterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> gameCharacterService.getCharacterById(99L));

        verify(gameCharacterRepository, times(1)).findById(99L);
    }

    @Test
    void shouldCreateCharacter() {
        GameCharacterCreateDTO dto = GameCharacterCreateDTO.builder()
                .gameId(1L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .build();

        GameCharacter newCharacter = new GameCharacter();
        newCharacter.setId(1L);
        newCharacter.setGame(game);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameCharacterFactory.fromCreateDto(dto, game)).thenReturn(newCharacter);
        when(gameCharacterRepository.save(any(GameCharacter.class))).thenReturn(newCharacter);

        GameCharacterDTO characterDto = GameCharacterDTO.builder()
                .id(1L)
                .gameId(1L)
                .name("Prélude")
                .description("Extension de Terraforming Mars.")
                .build();

        when(gameCharacterFactory.toDto(any(GameCharacter.class))).thenReturn(characterDto);

        GameCharacterDTO result = gameCharacterService.createCharacter(1L, dto);

        assertThat(result.name()).isEqualTo("Prélude");
        verify(gameRepository, times(1)).findById(1L);
        verify(gameCharacterRepository, times(1)).save(any(GameCharacter.class));
    }

    @Test
    void shouldUpdateCharacter() {
        GameCharacterUpdateDTO updateDto = GameCharacterUpdateDTO.builder()
                .name("Prélude Extended")
                .description("Nouvelle extension de Terraforming Mars.")
                .build();

        when(gameCharacterRepository.findById(1L)).thenReturn(Optional.of(gameCharacter));

        doAnswer(invocation -> {
            gameCharacter.setName("Prélude Extended");
            gameCharacter.setDescription("Nouvelle extension de Terraforming Mars.");
            return null;
        }).when(gameCharacterFactory).updateEntity(gameCharacter, updateDto);

        when(gameCharacterRepository.save(gameCharacter)).thenReturn(gameCharacter);

        GameCharacterDTO characterDto = GameCharacterDTO.builder()
                .id(1L)
                .gameId(1L)
                .name("Prélude Extended")
                .description("Nouvelle extension de Terraforming Mars.")
                .build();

        when(gameCharacterFactory.toDto(gameCharacter)).thenReturn(characterDto);

        GameCharacterDTO result = gameCharacterService.updateCharacter(1L, updateDto);

        assertThat(result.name()).isEqualTo("Prélude Extended");
    }

    @Test
    void shouldDeleteCharacter() {
        when(gameCharacterRepository.existsById(1L)).thenReturn(true);
        doNothing().when(gameCharacterRepository).deleteById(1L);

        gameCharacterService.deleteCharacter(1L);

        verify(gameCharacterRepository, times(1)).existsById(1L);
        verify(gameCharacterRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentCharacter() {
        when(gameCharacterRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> gameCharacterService.deleteCharacter(99L));

        verify(gameCharacterRepository, times(1)).existsById(99L);
        verify(gameCharacterRepository, never()).deleteById(any());
    }
}
