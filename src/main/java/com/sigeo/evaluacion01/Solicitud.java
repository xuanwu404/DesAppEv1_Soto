package com.sigeo.evaluacion01;

import java.util.Objects;
import java.util.Optional;

public record Solicitud(
        String id,
        String solicitante,
        String descripcion,
        Prioridad prioridad) {

    public Solicitud {
        validarTexto(id, "El id no puede ser nulo ni estar en blanco");
        validarTexto(solicitante, "El solicitante no puede ser nulo ni estar en blanco");
        validarTexto(descripcion, "La descripción no puede ser nula ni estar en blanco");
        Objects.requireNonNull(prioridad, "La prioridad no puede ser nula");
    }

    private static void validarTexto(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        Optional.of(valor)
                .filter(v -> !v.isBlank())
                .orElseThrow(() -> new IllegalArgumentException(mensajeError));
    }
}