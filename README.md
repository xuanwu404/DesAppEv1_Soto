# Evaluación Práctica N.º 1 — Gestor de Solicitudes SIGEO

Módulo de consola desarrollado en Java 25 para la gestión, consulta, agregación estadística y exportación de solicitudes operativas en memoria.

---

## 1. Verificación de Entorno y Trazabilidad

* **Lenguaje:** Java SE 25 (LTS) — `openjdk 25.0.4 2026-07-21 LTS`
* **Gestor de Construcción:** Apache Maven 3.9.6 (mediante Maven Wrapper `mvnw`)
* **Framework de Pruebas:** JUnit 5 Jupiter (versión 5.10.2)
* **Rama Git de Entrega:** `evaluacion-01/soto`
* **Estado de Pruebas:** `BUILD SUCCESS` (7 tests ejecutados, 0 fallos, 0 errores)

---

## 2. Arquitectura y Modelo de Dominio

El diseño respeta el contrato mínimo de firmas y prescinde de dependencias externas o bases de datos relacionales:

* **`Prioridad` (Enum):** Modela los niveles de urgencia (`BAJA`, `MEDIA`, `ALTA`, `CRITICA`) y encapsula su plazo referencial en horas mediante el método `horasAtencion()`.
* **`Solicitud` (Record):** Estructura inmutable que garantiza consistencia desde su instanciación mediante validación de invariantes en su constructor compacto (`!isBlank()`, `Objects.requireNonNull`).
* **`SolicitudDuplicadaException` (Excepción):** Hereda de `RuntimeException` para señalar violaciones de negocio al intentar registrar IDs repetidos.
* **`GestorSolicitudes` (Servicio en memoria):**
  * Registro controlado sin duplicados mediante pipelines funcionales.
  * Búsquedas con `Optional` y manejo de `NoSuchElementException` para IDs inexistentes.
  * Filtrado inmutable con Streams (`.toList()`) garantizando el encapsulamiento de la lista interna.
  * Agrupación estadística mediante `Collectors.groupingBy` y `Collectors.counting()` retornando `Map<Prioridad, Long>`.
  * Exportación de reportes a disco en formato delimitado por comas con codificación `StandardCharsets.UTF_8` y gestión segura de buffers (`try-with-resources`).
* **`App` (Punto de entrada):** Demostración del flujo completo con captura selectiva de excepciones y mensajes informativos en consola.

---

## 3. Guía de Ejecución en Consola (PowerShell)

### Ejecutar la suite completa de pruebas unitarias
```powershell
.\mvnw.cmd clean test