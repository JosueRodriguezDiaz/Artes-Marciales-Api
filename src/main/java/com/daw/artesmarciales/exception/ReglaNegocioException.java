package com.daw.artesmarciales.exception;

/**
 * Excepción personalizada de dominio para violaciones de reglas de negocio propias.
 * Será interceptada para devolver HTTP 422 Unprocessable Entity.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
