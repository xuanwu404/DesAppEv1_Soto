package com.sigeo.evaluacion01;

import java.util.Objects;

public record Solicitud(
        String id,
        String solicitante,
        String descripcion,
        Prioridad prioridad) {

    public Solicitud {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id no puede ser nulo ni estar en blanco");
        }
        if (solicitante == null || solicitante.isBlank()) {
            throw new IllegalArgumentException("El solicitante no puede ser nulo ni estar en blanco");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede ser nula ni estar en blanco");
        }
        Objects.requireNonNull(prioridad, "La prioridad no puede ser nula");
    }
}