package com.boardgame.controller;

import com.boardgame.integration.bgg.BggClient;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController // Combine @Controller + @ResponseBody -> chaque méthode renvoie directement du JSON dans la réponse HTTP
@RequestMapping("/api/bgg") // Préfixe appliqué à toutes les routes de ce contrôleur
@RequiredArgsConstructor // Génère automatiquement un constructeur avec les champs `final` (injection par constructeur)
public class BggController {

    // Injecté automatiquement par Spring grâce au constructeur généré par Lombok
    private final BggClient bggClient;

    /**
     * GET /api/bgg/thing/{id}
     * Appelle l’API XML de BoardGameGeek, convertit la réponse XML en JSON (JsonNode)
     * et renvoie ce JSON au client HTTP.
     */
    @GetMapping("/thing/{id}")
    public Mono<ResponseEntity<JsonNode>> getThingAsJson(
            @PathVariable      // Récupère la valeur {id} de l'URL
            @Positive long id  // Validation Jakarta : l'id doit être strictement > 0 (sinon 400 automatiquement)
    ) {
        // Appelle la couche d’intégration (client externe) et map la réponse en HTTP 200 OK + JSON
        return bggClient.fetchThingAsJson(id)
                .map(ResponseEntity::ok);
    }
}
