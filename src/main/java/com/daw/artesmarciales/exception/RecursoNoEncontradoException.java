package com.daw.artesmarciales.exception;

/**
 * Excepción personalizada lanzada cuando se solicita un Arte Marcial por ID
 * y no existe en la base de datos. Será interceptada para devolver HTTP 404 Not Found.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
