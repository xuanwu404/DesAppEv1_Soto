package com.sigeo.evaluacion01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GestorSolicitudesTest {

    private GestorSolicitudes gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorSolicitudes();
    }

    @Test
    @DisplayName("1. Crea una solicitud válida")
    void testCreaSolicitudValida() {
        Solicitud s = new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", Prioridad.ALTA);
        assertEquals("SOL-001", s.id());
        assertEquals("Cabo Rojas", s.solicitante());
        assertEquals("Reponer radio", s.descripcion());
        assertEquals(Prioridad.ALTA, s.prioridad());
        assertEquals(24, s.prioridad().horasAtencion());
    }

    @Test
    @DisplayName("2. Rechaza id vacío o descripción vacía")
    void testRechazaInvariantesInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> new Solicitud("", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));

        assertThrows(IllegalArgumentException.class,
                () -> new Solicitud("   ", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));

        assertThrows(IllegalArgumentException.class,
                () -> new Solicitud("SOL-001", "Cabo Rojas", "   ", Prioridad.ALTA));

        assertThrows(NullPointerException.class, () -> new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", null));
    }

    @Test
    @DisplayName("3. Registra y busca por id")
    void testRegistraYBuscaPorId() {
        Solicitud s = new Solicitud("SOL-002", "Sgto. Muñoz", "Revisar generador", Prioridad.MEDIA);
        gestor.registrar(s);

        Solicitud encontrada = gestor.buscarPorId("SOL-002");
        assertNotNull(encontrada);
        assertEquals("Sgto. Muñoz", encontrada.solicitante());

        assertThrows(NoSuchElementException.class, () -> gestor.buscarPorId("SOL-999"));
    }

    @Test
    @DisplayName("4. Rechaza un id duplicado")
    void testRechazaIdDuplicado() {
        gestor.registrar(new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));

        SolicitudDuplicadaException ex = assertThrows(SolicitudDuplicadaException.class,
                () -> gestor.registrar(new Solicitud("SOL-001", "Otro", "Otra tarea", Prioridad.BAJA)));

        assertTrue(ex.getMessage().contains("SOL-001"));
    }

    @Test
    @DisplayName("5. Filtra por prioridad sin mezclar resultados")
    void testFiltraPorPrioridad() {
        gestor.registrar(new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));
        gestor.registrar(new Solicitud("SOL-002", "Sgto. Muñoz", "Revisar generador", Prioridad.MEDIA));
        gestor.registrar(new Solicitud("SOL-003", "Cbo. Pérez", "Restablecer enlace", Prioridad.ALTA));

        List<Solicitud> altas = gestor.filtrarPorPrioridad(Prioridad.ALTA);
        assertEquals(2, altas.size());
        assertTrue(altas.stream().allMatch(s -> s.prioridad() == Prioridad.ALTA));
    }

    @Test
    @DisplayName("6. Cuenta solicitudes por prioridad")
    void testCuentaPorPrioridad() {
        gestor.registrar(new Solicitud("SOL-001", "A", "Desc 1", Prioridad.ALTA));
        gestor.registrar(new Solicitud("SOL-002", "B", "Desc 2", Prioridad.MEDIA));
        gestor.registrar(new Solicitud("SOL-003", "C", "Desc 3", Prioridad.CRITICA));

        Map<Prioridad, Long> conteo = gestor.contarPorPrioridad();
        assertEquals(1L, conteo.get(Prioridad.ALTA));
        assertEquals(1L, conteo.get(Prioridad.MEDIA));
        assertEquals(1L, conteo.get(Prioridad.CRITICA));
        assertNull(conteo.get(Prioridad.BAJA));
    }

    @Test
    @DisplayName("7. Exporta y permite leer el archivo generado en @TempDir")
    void testExportarReporte(@TempDir Path tempDir) throws IOException {
        gestor.registrar(new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));
        Path archivoDestino = tempDir.resolve("reporte-solicitudes.txt");

        gestor.exportarReporte(archivoDestino);
        assertTrue(Files.exists(archivoDestino));

        List<String> lineas = Files.readAllLines(archivoDestino, StandardCharsets.UTF_8);
        assertEquals(2, lineas.size());
        assertEquals("ID,SOLICITANTE,DESCRIPCION,PRIORIDAD,HORAS_ATENCION", lineas.get(0));
        assertTrue(lineas.get(1).contains("SOL-001"));
    }
}