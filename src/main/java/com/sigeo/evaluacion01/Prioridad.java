package com.sigeo.evaluacion01;

public enum Prioridad {
    BAJA(72),
    MEDIA(48),
    ALTA(24),
    CRITICA(4);

    private final int horasAtencion;

    Prioridad(int horasAtencion) {
        this.horasAtencion = horasAtencion;
    }

    public int horasAtencion() {
        return horasAtencion;
    }
}