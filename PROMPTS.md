# Bitácora de Interacción con IA — PROMPTS.md

## Plan Inicial y Estrategia de Desarrollo
Para abordar los requerimientos técnicos de la Evaluación Práctica N.º 1 del módulo SIGEO, se definió un flujo de trabajo iterativo e incremental basado en Java 25 y Maven:
1. **Configuración de Entorno:** Verificación de compatibilidad con Java 25 (LTS) y Maven Wrapper.
2. **Modelado de Dominio:** Implementación inmutable de `Prioridad` y `Solicitud` con validación estricta de invariantes.
3. **Lógica de Negocio y Persistencia Volátil:** Creación de `GestorSolicitudes` en memoria con operaciones de búsqueda, filtrado y conteo declarativo.
4. **Manejo de Errores y E/S:** Definición de `SolicitudDuplicadaException`, exportación en codificación UTF-8 y controlador de consola en `App.java`.
5. **Aseguramiento de Calidad:** Construcción de pruebas unitarias automatizadas con JUnit 5 y validación cruzada con `@TempDir`.

---

## Registro de Interacciones y Decisiones Críticas

### Prompt 1: Planificación inicial de arquitectura y dependencias
- **Prompt:** "Se requiere estructurar un proyecto Maven en Java 25 para un módulo de consola sin persistencia externa ni frameworks pesados como Spring. ¿Cuál es la configuración mínima del pom.xml requerida para habilitar la compilación en release 25 y ejecutar pruebas con JUnit 5?"
- **Sugerencia IA:** Propuso un archivo `pom.xml` con propiedades `<maven.compiler.release>25</maven.compiler.release>` y la dependencia `org.junit.jupiter:junit-jupiter` en versión 5.10.2 con alcance `test`.
- **Decisión técnica del estudiante (Aceptada):** Se configuró el archivo base asegurando que el compilador y Surefire no requirieran dependencias ajenas a la especificación estándar de la evaluación.

### Prompt 2: Modelado del enum Prioridad y encapsulamiento
- **Prompt:** "Para el dominio del problema se definen cuatro prioridades operativas con plazos referenciales de atención en horas: BAJA (72), MEDIA (48), ALTA (24) y CRITICA (4). ¿Cómo diseñar el enum de modo que exponga el método horasAtencion() respetando encapsulamiento y evitando valores mutables?"
- **Sugerencia IA:** Sugirió implementar un enum con un atributo `private final int horasAtencion`, un constructor parametrizado y el método accessor público correspondiente.
- **Decisión técnica del estudiante (Aceptada):** Se implementó tal como se propuso, garantizando que los tiempos de atención queden protegidos contra modificaciones accidentales en tiempo de ejecución.

### Prompt 3: Validación de invariantes en el Record Solicitud
- **Prompt:** "En relación con los requisitos sobre el modelo de dominio, se requiere validar que los campos id, solicitante y descripción del record Solicitud no sean nulos ni queden en blanco, y que la prioridad no sea nula. ¿Cuál es la implementación recomendada para el constructor compacto?"
- **Sugerencia IA:** Planteó validar las cadenas de texto empleando el método tradicional `.isEmpty()`.
- **Decisión técnica del estudiante (Corregida):** Se rechazó `.isEmpty()` y se sustituyó por `.isBlank()`, dado que `.isEmpty()` permite cadenas formadas únicamente por espacios en blanco (`"   "`), violando las reglas de consistencia de datos.

### Prompt 4: Excepción personalizada para control de duplicados
- **Prompt:** "Se necesita modelar una excepción para rechazar solicitudes con IDs repetidos en memoria. ¿Es más recomendable extender de Exception o de RuntimeException considerando la arquitectura del gestor?"
- **Sugerencia IA:** Recomendó crear `SolicitudDuplicadaException` heredando de `RuntimeException` para modelar una violación de invariante de negocio no comprobada (*unchecked*), evitando la propagación obligatoria de firmas checked innecesarias.
- **Decisión técnica del estudiante (Aceptada):** Se implementó la clase extendiendo `RuntimeException` y pasando el mensaje contextual con el ID infractor a la clase padre.

### Prompt 5: Manejo de inmutabilidad en búsquedas y filtros
- **Prompt:** "En GestorSolicitudes, ¿cómo estructurar el método filtrarPorPrioridad para retornar las solicitudes correspondientes garantizando que la colección interna no pueda ser alterada externamente por el llamador?"
- **Sugerencia IA:** Sugirió retornar la lista filtrada directamente mediante `return solicitudes.stream().filter(s -> s.prioridad() == prioridad).toList();`.
- **Decisión técnica del estudiante (Aceptada):** El método `.toList()` de Java 16+ retorna una lista inmutable, resolviendo el principio de encapsulamiento sin requerir copias defensivas manuales.

### Prompt 6: Búsqueda controlada de ID inexistente
- **Prompt:** "¿Cuál es la forma canónica de implementar buscarPorId en Java moderno cuando un elemento no existe en la colección, cumpliendo con reportar un error controlado y un mensaje útil?"
- **Sugerencia IA:** Utilizar `solicitudes.stream().filter(...).findFirst().orElseThrow(() -> new NoSuchElementException("..."))`.
- **Decisión técnica del estudiante (Aceptada):** Se adoptó el patrón funcional con `NoSuchElementException`, asegurando que la ausencia del registro lance una excepción estándar y semánticamente adecuada.

### Prompt 7: Agrupamiento estadístico con Streams
- **Prompt:** "Considerando las restricciones de arquitectura sin persistencia externa, ¿cómo estructurar el método contarPorPrioridad para retornar un Map<Prioridad, Long> utilizando la API de Streams de Java de forma declarativa?"
- **Sugerencia IA:** Propuso el uso del colector `Collectors.groupingBy(Solicitud::prioridad, Collectors.counting())`.
- **Decisión técnica del estudiante (Aceptada):** Se implementó la solución en una sola línea declarativa, garantizando legibilidad y optimización en un solo recorrido de la lista.

### Prompt 8: Exportación de archivos y control de E/S en UTF-8
- **Prompt:** "Para el método exportarReporte(Path destino), ¿cómo garantizar que el archivo se escriba con codificación UTF-8, encabezados delimitados por coma y cierre automático de recursos ante fallas de disco?"
- **Sugerencia IA:** Planteó el uso de `Files.newBufferedWriter(destino, StandardCharsets.UTF_8)` dentro de una estructura `try-with-resources` y formateo de cadenas con `String.format`.
- **Decisión técnica del estudiante (Aceptada):** Se aseguró la portabilidad del reporte frente a caracteres especiales (ej. tildes o 'ñ') y el manejo seguro del descriptor de archivo.

### Prompt 9: Cobertura de pruebas unitarias y aislamiento con @TempDir
- **Prompt:** "Conforme a la sección de pruebas de la rúbrica, se requiere auditar GestorSolicitudesTest para verificar la cobertura de los 7 casos exigidos, incluyendo la validación de exportación de archivos en un entorno temporal aislado."
- **Sugerencia IA:** Planteó una suite de 7 métodos `@Test` con `@DisplayName` expresivos y el uso del parámetro inyectado `@TempDir Path tempDir` para la prueba de exportación.
- **Decisión técnica del estudiante (Aceptada):** Se implementó la suite completa asegurando que la prueba de E/S no genere artefactos residuales en el sistema de archivos del entorno de ejecución.

### Prompt 10: Refactorización hacia patrones funcionales y reducción de condicionales
- **Prompt:** "En función de las buenas prácticas de desarrollo en Java moderno y la optimización de código en entornos profesionales, ¿cómo refactorizar las validaciones y búsquedas en Solicitud y GestorSolicitudes para prescindir de bloques condicionales imperativos (if)?"
- **Sugerencia IA:** Implementar utilidades estáticas de `java.util.Objects`, encadenamiento de `Optional.filter` para validación de invariantes y pipelines con `ifPresent` para la detección de duplicados.
- **Decisión técnica del estudiante (Aceptada):** Se adoptó el enfoque declarativo asegurando que no se modificaran las firmas públicas del contrato inicial y validando que los 7 tests mantuvieran el resultado `BUILD SUCCESS`.

---

## Resumen de Decisiones del Estudiante vs Sugerencias de IA

| Área de Decisión | Sugerencia Inicial de IA | Decisión / Corrección Técnica Aplicada | Justificación Técnica |
| :--- | :--- | :--- | :--- |
| **Validación de Texto** | Uso de `s.isEmpty()` | Sustitución por `!s.isBlank()` | Impide registrar campos con sólo espacios en blanco. |
| **Control de Duplicados** | Búsqueda booleana con `if (existe)` | Pipeline `Stream.findFirst().ifPresent(...)` | Adopta programación funcional y elimina bifurcaciones imperativas. |
| **Manejo de Errores en Consola** | Uso de `e.printStackTrace()` | Captura selectiva en `App.java` con salida `System.err` | Evita exponer trazas crudas y cumple el criterio de manejo limpio. |
| **Entorno de Pruebas de Disco** | Escritura directa en carpeta raíz | Uso de `@TempDir` en JUnit 5 | Aísla los tests unitarios y evita efectos colaterales en disco. |