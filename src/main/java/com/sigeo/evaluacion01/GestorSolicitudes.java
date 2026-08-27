package com.sigeo.evaluacion01;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class GestorSolicitudes {

    private final List<Solicitud> solicitudes = new ArrayList<>();

    public void registrar(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }
        boolean existe = solicitudes.stream()
                .anyMatch(s -> s.id().equalsIgnoreCase(solicitud.id()));
        if (existe) {
            throw new SolicitudDuplicadaException("Ya existe una solicitud con el id: " + solicitud.id());
        }
        solicitudes.add(solicitud);
    }

    public Solicitud buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de búsqueda no puede ser nulo ni estar en blanco");
        }
        return solicitudes.stream()
                .filter(s -> s.id().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró la solicitud con id: " + id));
    }

    public List<Solicitud> filtrarPorPrioridad(Prioridad prioridad) {
        return solicitudes.stream()
                .filter(s -> s.prioridad() == prioridad)
                .toList();
    }

    public Map<Prioridad, Long> contarPorPrioridad() {
        return solicitudes.stream()
                .collect(Collectors.groupingBy(Solicitud::prioridad, Collectors.counting()));
    }

    public void exportarReporte(Path destino) throws IOException {
        if (destino == null) {
            throw new IllegalArgumentException("La ruta de destino no puede ser nula");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            writer.write("ID,SOLICITANTE,DESCRIPCION,PRIORIDAD,HORAS_ATENCION");
            writer.newLine();
            for (Solicitud s : solicitudes) {
                writer.write(String.format("%s,%s,%s,%s,%d",
                        s.id(),
                        s.solicitante(),
                        s.descripcion(),
                        s.prioridad().name(),
                        s.prioridad().horasAtencion()));
                writer.newLine();
            }
        }
    }
}