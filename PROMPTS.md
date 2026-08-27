# Bitácora de Interacción con IA — PROMPTS.md

## Plan Inicial
Desarrollo guiado por pruebas e incremental en Java 25:
1. Modelado de Dominio (`Prioridad`, `Solicitud` con constructor compacto e invariantes).
2. Lógica del Gestor en memoria (`GestorSolicitudes`) y excepciones (`SolicitudDuplicadaException`).
3. Exportación E/S UTF-8 y aplicación de consola (`App.java`).
4. Pruebas unitarias JUnit 5 y verificación con Maven Wrapper.

## Interacciones y Decisiones Críticas

### Prompt 1: Validación e invariantes en Record
- **Prompt:** "Revisa mis invariantes para el record Solicitud. Valida que id, solicitante y descripción no sean nulos ni vacíos, y prioridad no sea nula."
- **Sugerencia IA:** Propuso usar `.isEmpty()` directamente sobre las cadenas.
- **Decisión del estudiante (Corregida):** Se sustituyó por `.isBlank()` para asegurar que cadenas compuestas únicamente de espacios en blanco también sean rechazadas conforme a la pauta.

### Prompt 2: Agrupamiento con Streams en GestorSolicitudes
- **Prompt:** "¿Cómo implementar contarPorPrioridad devolviendo un Map<Prioridad, Long> usando Java Streams?"
- **Sugerencia IA:** `solicitudes.stream().collect(Collectors.groupingBy(Solicitud::prioridad, Collectors.counting()));`
- **Decisión del estudiante (Aceptada):** Solución concisa, inmutable e idiomática que evita mutaciones de colecciones externas.

### Prompt 3: Auditoría y cobertura de JUnit 5
- **Prompt:** "Revisa GestorSolicitudesTest.java para cubrir los 7 casos exigidos en la pauta de evaluación."
- **Sugerencia IA:** Suite con 7 pruebas unitarias usando `@TempDir` para E/S.
- **Decisión del estudiante (Aceptada):** Se adoptó la suite completa asegurando aislamiento en disco temporal.