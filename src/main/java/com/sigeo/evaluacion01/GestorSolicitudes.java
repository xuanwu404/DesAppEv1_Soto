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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestorSolicitudes {

    private final List<Solicitud> solicitudes = new ArrayList<>();

    public void registrar(Solicitud solicitud) {
        Solicitud s = Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");

        solicitudes.stream()
                .filter(existente -> existente.id().equalsIgnoreCase(s.id()))
                .findFirst()
                .ifPresent(duplicado -> {
                    throw new SolicitudDuplicadaException("Ya existe una solicitud con el id: " + duplicado.id());
                });

        solicitudes.add(s);
    }

    public Solicitud buscarPorId(String id) {
        return Optional.ofNullable(id)
                .filter(valor -> !valor.isBlank())
                .flatMap(valorId -> solicitudes.stream()
                        .filter(s -> s.id().equalsIgnoreCase(valorId))
                        .findFirst())
                .orElseThrow(() -> Optional.ofNullable(id).filter(v -> !v.isBlank()).isPresent()
                        ? new NoSuchElementException("No se encontró la solicitud con id: " + id)
                        : new IllegalArgumentException("El id de búsqueda no puede ser nulo ni estar en blanco"));
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
        Path rutaDestino = Objects.requireNonNull(destino, "La ruta de destino no puede ser nula");

        try (BufferedWriter writer = Files.newBufferedWriter(rutaDestino, StandardCharsets.UTF_8)) {
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