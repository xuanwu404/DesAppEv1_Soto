# Evaluación Práctica N.º 1 — SIGEO
**Módulo:** Gestor básico de solicitudes SIGEO  
**Entorno:** Java 25 (LTS) · Apache Maven 3.9.6 · JUnit 5.10.2  

## Verificación de Entorno y Pruebas
- **Versión de Java:** `openjdk 25.0.1 2025-10-21 LTS`
- **Ejecución de Pruebas:**
  \`\`\`bash
  .\mvnw.cmd clean test
  \`\`\`
- **Resultado:** `BUILD SUCCESS` (7 tests ejecutados y aprobados sin fallos).

## Estructura del Dominio
- `Prioridad`: Enum con horas de atención (`BAJA`: 72, `MEDIA`: 48, `ALTA`: 24, `CRITICA`: 4).
- `Solicitud`: Record inmutable con validación de invariantes en constructor compacto (`isBlank()`, non-null).
- `GestorSolicitudes`: Manejo en memoria, Streams inmutables y exportación en UTF-8.