package com.logitrack.logitrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une opération viole une règle métier.
 * Provoque une réponse HTTP 400 (BAD_REQUEST).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    /**
     * Constructeur par défaut.
     */
    public BusinessException() {
        super();
    }

    /**
     * Constructeur avec un message personnalisé.
     * C'est ce constructeur qui corrigeait votre erreur de compilation.
     *
     * @param message Le message détaillant l'erreur.
     */
    public BusinessException(String message) {
        super(message);
    }
}