# Bitácora de Interacción con IA — PROMPTS.md

## Plan Inicial
Desarrollo guiado por pruebas e incremental en Java 25 conforme a la especificación técnica:
1. Modelado de Dominio (`Prioridad`, `Solicitud` con constructor compacto e invariantes).
2. Lógica del Gestor en memoria (`GestorSolicitudes`) y excepciones controladas (`SolicitudDuplicadaException`).
3. Exportación E/S en UTF-8 y aplicación de consola (`App.java`).
4. Pruebas unitarias con JUnit 5 y verificación automatizada mediante Maven Wrapper.

## Interacciones y Decisiones Críticas

### Prompt 1: Validación e invariantes en Record
- **Prompt:** "En relación con los requisitos de la rúbrica sobre el modelo de dominio, se requiere validar que los campos id, solicitante y descripción del record Solicitud no sean nulos ni queden en blanco, y que la prioridad no sea nula. ¿Cuál es la implementación recomendada para el constructor compacto?"
- **Sugerencia IA:** Propuso una validación inicial empleando el método `.isEmpty()` sobre las cadenas de texto.
- **Decisión técnica del estudiante (Corregida):** Se descartó `.isEmpty()` y se sustituyó por `.isBlank()`, garantizando que aquellas cadenas compuestas únicamente por espacios en blanco sean rechazadas conforme a la regla de invariantes de la pauta.

### Prompt 2: Agrupamiento y procesamiento funcional con Streams
- **Prompt:** "Considerando las restricciones de arquitectura sin persistencia externa, ¿cómo estructurar el método contarPorPrioridad para retornar un Map<Prioridad, Long> utilizando la API de Streams de Java de forma idiomática?"
- **Sugerencia IA:** Propuso el uso del colector `Collectors.groupingBy(Solicitud::prioridad, Collectors.counting())`.
- **Decisión técnica del estudiante (Aceptada):** Se adoptó la solución funcional propuesta al permitir una operación declarativa de paso único, evitando mutaciones directas sobre la colección interna.

### Prompt 3: Cobertura de pruebas unitarias y aislamiento de E/S
- **Prompt:** "Conforme a la sección de pruebas de la rúbrica, se requiere auditar GestorSolicitudesTest para verificar la cobertura de los 7 casos exigidos, incluyendo la validación de exportación de archivos en un entorno temporal aislado."
- **Sugerencia IA:** Planteó una suite de 7 pruebas unitarias estructuradas con JUnit 5 empleando la extensión `@TempDir` para la prueba de exportación.
- **Decisión técnica del estudiante (Aceptada):** Se implementó la suite completa asegurando que la prueba de E/S no genere artefactos residuales en el sistema de archivos del entorno de ejecución.

### Prompt 4: Refactorización hacia patrones funcionales y reducción de condicionales
- **Prompt:** "En función de las buenas prácticas de desarrollo en Java moderno y la optimización de código en entornos profesionales, ¿cómo refactorizar las validaciones y búsquedas en Solicitud y GestorSolicitudes para prescindir de bloques condicionales imperativos (if)?"
- **Sugerencia IA:** Implementar utilidades estáticas de `java.util.Objects`, encadenamiento de `Optional.filter` para validación de invariantes y pipelines con `ifPresent` para la detección de duplicados.
- **Decisión técnica del estudiante (Aceptada):** Se adoptó el enfoque declarativo asegurando que no se modificaran las firmas públicas del contrato inicial y validando que los 7 tests mantuvieran el resultado `BUILD SUCCESS`.