package com.boardgame.service;

import com.boardgame.model.dto.player.PlayerCreateDTO;
import com.boardgame.model.dto.player.PlayerDTO;
import com.boardgame.model.dto.player.PlayerUpdateDTO;
import com.boardgame.model.entity.Player;
import com.boardgame.model.factory.PlayerFactory;
import com.boardgame.repository.PlayerRepository;
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
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerFactory playerFactory;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setPlayerId(1L);
        player.setPlayerName("Thibaud");
        player.setAvatar("");
        player.setFavoriteGame("Pandemie");
    }

    @Test
    void shouldReturnAllPlayers() {
        when(playerRepository.findAll()).thenReturn(List.of(player));
        PlayerDTO playerDto = PlayerDTO.builder()
                .playerId(1L)
                .playerName("Thibaud")
                .avatar("test")
                .favoriteGame("Catan")
                .build();
        when(playerFactory.toDto(player)).thenReturn(playerDto);

        List<PlayerDTO> result = playerService.getAllPLayers();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().playerName()).isEqualTo("Thibaud");
        assertThat(result.getFirst().favoriteGame()).isEqualTo("Catan");
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnPlayerById() {
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        PlayerDTO playerDto = PlayerDTO.builder()
                .playerId(1L)
                .playerName("Sophie")
                .avatar("test de description")
                .favoriteGame("Catan")
                .build();
        when(playerFactory.toDto(player)).thenReturn(playerDto);

        PlayerDTO dto = playerService.getPlayerById(1L);

        assertThat(dto.playerName()).isEqualTo("Sophie");
        verify(playerRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenPlayerNotFound() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> playerService.getPlayerById(99L)
        );
    }

    @Test
    void shouldCreatePlayer() {
        PlayerCreateDTO playerCreateDto = PlayerCreateDTO.builder()
                .playerName("Sophie")
                .avatar("test de description")
                .favoriteGame("Catan")
                .build();

        Player newPlayer = new Player();
        newPlayer.setPlayerId(1L);

        when(playerFactory.fromCreateDto(playerCreateDto)).thenReturn(newPlayer);
        when(playerRepository.save(any(Player.class))).thenReturn(newPlayer);
        PlayerDTO playerDto = PlayerDTO.builder()
                .playerName("Sophie")
                .avatar("test de description")
                .favoriteGame("Catan")
                .build();
        when(playerFactory.toDto(any(Player.class))).thenReturn(playerDto);

        PlayerDTO result = playerService.createPlayer(playerCreateDto);

        assertThat(result.playerName()).isEqualTo("Sophie");
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void shouldUpdatePlayer() {
        PlayerUpdateDTO playerUpdatedto = PlayerUpdateDTO.builder()
                .playerName("Thibaud")
                .avatar("test de description numero 2")
                .favoriteGame("Catan")
                .build();

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        doAnswer(invocation -> {
            player.setPlayerName("Thibaud");
            player.setAvatar("Nouvelle description numéro 2");
            player.setFavoriteGame("Catan");
            return null;
        }).when(playerFactory).updateEntity(player, playerUpdatedto);

        when(playerRepository.save(player)).thenReturn(player);
        PlayerDTO playerDTO = PlayerDTO.builder()
                .playerId(1L)
                .playerName("Thibaud")
                .avatar("Colonise Mars et fais prospérer ton entreprise.")
                .favoriteGame("Pandemie")
                .build();
        when(playerFactory.toDto(player)).thenReturn(playerDTO);

        PlayerDTO result = playerService.updatePlayer(1L, playerUpdatedto);

        assertThat(result.playerName()).isEqualTo("Thibaud");
        assertThat(result.avatar()).isEqualTo("Colonise Mars et fais prospérer ton entreprise.");
        assertThat(result.favoriteGame()).isEqualTo("Pandemie");
    }

    @Test
    void shouldDeletePlayer() {
        when(playerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(playerRepository).deleteById(1L);

        playerService.deletePlayer(1L);

        verify(playerRepository, times(1)).existsById(1L);
        verify(playerRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentPlayer() {
        when(playerRepository.existsById(99L)).thenReturn(false);

        assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> playerService.deletePlayer(99L)
        );

        verify(playerRepository, times(1)).existsById(99L);
        verify(playerRepository, never()).deleteById(any());
    }
}
