package com.sigeo.evaluacion01;

public class SolicitudDuplicadaException extends RuntimeException {
    public SolicitudDuplicadaException(String mensaje) {
        super(mensaje);
    }
}