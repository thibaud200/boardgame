package com.boardgame.repository;

import com.boardgame.model.entity.GameExpansion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameExpansionRepository extends JpaRepository<GameExpansion, Long> {
    // ex : récupérer les extensions d’un jeu
    List<GameExpansion> findByGame_GameId(Long gameId);
}