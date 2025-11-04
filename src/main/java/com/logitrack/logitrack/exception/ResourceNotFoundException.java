package com.logitrack.logitrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée.
 * Provoque une réponse HTTP 404 (NOT_FOUND).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructeur par défaut.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Constructeur avec un message personnalisé.
     * C'est ce constructeur qui corrigeait votre erreur de compilation.
     *
     * @param message Le message détaillant l'erreur.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}