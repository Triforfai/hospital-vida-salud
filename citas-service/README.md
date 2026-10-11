
# Hospital Vida Salud - Citas Service

Microservicio reactivo para la gestión de citas médicas.

## Tecnologías

- Java 25
- Spring Boot 4.1.1
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Spring Cloud Eureka
- RabbitMQ
- Maven
- GitHub Actions

## Funcionalidades

- Registrar citas médicas.
- Consultar todas las citas y buscar por identificador.
- Validar que el paciente exista mediante el microservicio de Pacientes.
- Crear citas con estado PENDIENTE.
- Confirmar citas pendientes.
- Rechazar una segunda confirmación.
- Publicar el evento CitaConfirmada en RabbitMQ.
- Ejecutar pruebas automatizadas con JUnit y Mockito.

## Endpoints

| Método | Ruta | Función |
|---|---|---|
| POST | /api/citas | Crear una cita |
| GET | /api/citas | Listar citas |
| GET | /api/citas/{id} | Consultar una cita |
| POST | /api/citas/{id}/confirmar | Confirmar una cita pendiente |

## Arquitectura

Las solicitudes entran por API Gateway, en el puerto 8080, y se dirigen a citas-service, en el puerto 8082.

El servicio consulta PostgreSQL mediante R2DBC y valida al paciente mediante Eureka y WebClient.

Al confirmar una cita, publica el evento CitaConfirmada en RabbitMQ. Los consumidores de Notificaciones y Auditoría reciben sus respectivas copias. Auditoría guarda los eventos en PostgreSQL.

## Ejecución local

1. Tener Java 25 y Docker disponibles.
2. Iniciar PostgreSQL, RabbitMQ y Eureka.
3. Configurar las credenciales locales en application.yml usando application.example.yml como referencia.
4. Ejecutar las pruebas con:

   `./mvnw --batch-mode verify`

5. Iniciar el microservicio desde IntelliJ o con Maven.

## Pruebas automatizadas

GitHub Actions ejecuta la compilación y las pruebas automáticamente al realizar un push a main o abrir un pull request dirigido a main.