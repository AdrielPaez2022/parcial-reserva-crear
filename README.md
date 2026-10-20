# Examen Parcial 1
## Ing. y Lic. en Sistemas
### Universidad Nacional de Chilecito

# Examen parcial: crear una reserva

## Objetivo

Desarrollar una entidad de dominio y un caso de uso para el Core de una aplicación, aplicando Test-Driven Development (TDD), Clean Architecture y principios SOLID. Se evaluará el diseño, las pruebas y un historial de commits que demuestre que las pruebas se escribieron antes que el código de producción.

## Consigna

En este proyecto Maven, implementá en el módulo `core` la entidad `Reserva` y el caso de uso `CreateReservationUseCase`. Mantené la lógica de negocio y los contratos dentro del Core, sin depender de frameworks, bases de datos ni implementaciones concretas de infraestructura.

### Entidad `Reserva`

La entidad contiene:

- `id`: identificador de tipo `UUID`, recibido como dato de entrada.
- `emailContacto`: correo de contacto de quien realiza la reserva.
- `fecha`: fecha de la reserva (`LocalDate`).
- `cantidadPersonas`: cantidad de personas incluidas.
- `estado`: estado de la reserva (`ACTIVA` o `CANCELADA`).

Creá las instancias mediante el factory method `Reserva.instance`. Definí y probá estas reglas elementales:

- El identificador y la fecha son obligatorios.
- El correo de contacto es obligatorio y no puede estar vacío.
- La cantidad de personas debe ser mayor que cero.
- Una reserva nueva comienza en estado `ACTIVA`.
- Una reserva cancelada no puede cancelarse nuevamente.

Cuando una regla no se cumpla, informá el error mediante excepciones personalizadas. Las pruebas deben verificar el tipo de excepción esperado.

### Caso de uso `CreateReservationUseCase`

El caso de uso debe:

1. Crear la entidad `Reserva` con los datos recibidos.
2. Consultar mediante el puerto `ReservationRepository` si ya existe una reserva activa para el mismo correo de contacto y fecha. Si existe, lanzar `ReservationAlreadyExistsException`.
3. Solicitar el guardado de la reserva. Si no se puede guardar, lanzar `PersistenceException`.
4. Devolver la reserva creada.

## Arquitectura

- Ubicá la entidad, el caso de uso, las excepciones y el contrato `ReservationRepository` dentro del módulo `core`.
- Hacé que el caso de uso dependa de abstracciones, no de implementaciones de persistencia ni de Spring.
- Mantené las reglas de negocio en la entidad y coordiná su ejecución desde el caso de uso.
- El alcance se limita al Core: no implementes endpoints HTTP ni persistencia concreta.

La estructura de referencia del Core es:

```text
core/src/main/java/reserva/
├── exception/
├── input/
├── modelo/
├── output/
└── usecase/
```

## Pruebas y proceso obligatorio de TDD

Las pruebas unitarias del caso de uso deben usar un Mock o doble de prueba para `ReservationRepository` y cubrir el registro exitoso, una reserva duplicada y un fallo al guardar.

Seguí este ciclo, manteniendo separados los commits de pruebas y de código fuente:

1. Escribí las pruebas de `Reserva`, ejecutalas y comprobá que fallen por falta de implementación.
2. Hacé un commit con las pruebas de la entidad, sin código de producción.
3. Implementá `Reserva` y las excepciones necesarias; ejecutá las pruebas hasta que pasen y hacé un commit separado con el código fuente.
4. Escribí las pruebas de `CreateReservationUseCase`, ejecutalas y comprobá que fallen por falta de implementación.
5. Hacé un commit con las pruebas del caso de uso, sin su implementación.
6. Implementá el caso de uso y los cambios mínimos necesarios; ejecutá las pruebas hasta que pasen y hacé un commit separado con el código fuente.

Usá mensajes de commit claros, por ejemplo:

```text
test: agregar pruebas de creación y validación de Reserva
feat: implementar Reserva para satisfacer las pruebas
test: agregar pruebas de CreateReservationUseCase
feat: implementar CreateReservationUseCase
```

## Entrega

- Trabajá en una rama llamada `feature/parcial`.
- Abrí un único Pull Request desde `feature/parcial` hacia `main`.
- En la descripción del PR indicá las reglas de dominio, las excepciones utilizadas y cómo el historial de commits evidencia el proceso TDD.

## Criterios de evaluación

Se evaluará la cobertura de casos válidos e inválidos, la aplicación de las reglas de dominio, el uso de excepciones personalizadas, la dependencia del caso de uso respecto de un puerto, los escenarios de prueba solicitados y el orden de los commits.
