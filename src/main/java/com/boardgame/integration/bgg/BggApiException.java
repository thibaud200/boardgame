package com.boardgame.integration.bgg;

// Exception pour les erreurs d'API BGG (401, 403, 500, etc.)
public class BggApiException extends RuntimeException {
    public BggApiException(String message) {
        super(message);
    }

    public BggApiException(String message, Throwable cause) {
        super(message, cause);
    }
}