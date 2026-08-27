package com.sigeo.evaluacion01;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        GestorSolicitudes gestor = new GestorSolicitudes();

        try {
            gestor.registrar(new Solicitud("SOL-001", "Cabo Rojas", "Reponer radio", Prioridad.ALTA));
            gestor.registrar(new Solicitud("SOL-002", "Sgto. Muñoz", "Revisar generador", Prioridad.MEDIA));
            gestor.registrar(new Solicitud("SOL-003", "Cbo. Pérez", "Restablecer enlace", Prioridad.CRITICA));

            System.out.println("✅ Solicitudes registradas con éxito.");
            System.out.println("Resumen por prioridad: " + gestor.contarPorPrioridad());

            Path rutaReporte = Paths.get("reporte-solicitudes.txt");
            gestor.exportarReporte(rutaReporte);
            System.out.println("📄 Reporte exportado en: " + rutaReporte.toAbsolutePath());

        } catch (SolicitudDuplicadaException e) {
            System.err.println("Error de duplicidad: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de E/S al exportar reporte: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
}