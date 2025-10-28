package com.boardgame.repository;

import com.boardgame.model.entity.Game;
import com.boardgame.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository  extends JpaRepository<Player, Long> {
    List<Player> findByPlayerId(Long playerId);
}
