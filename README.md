# Hospital Vida Salud

Sistema de gestion hospitalaria basado en microservicios con Java y Spring Boot.

## Arquitectura

El sistema utiliza Eureka para el descubrimiento de servicios y Spring Cloud Gateway como punto de entrada de la API. PostgreSQL almacena los datos y RabbitMQ permite comunicar eventos de citas confirmadas a los servicios de notificaciones y auditoria.

## Tecnologias

- Java 25
- Spring Boot
- Spring Cloud Gateway
- Netflix Eureka
- Spring WebFlux: Mono y Flux
- Spring Data R2DBC
- PostgreSQL
- RabbitMQ
- Maven
- Git y GitHub Actions

## Microservicios y puertos

| Componente | Puerto |
|---|---:|
| API Gateway | 8080 |
| Pacientes | 8081 |
| Citas | 8082 |
| Notificaciones | 8083 |
| Auditoria | 8084 |
| Atenciones | 8085 |
| Laboratorio | 8086 |
| Hospitalizacion | 8087 |
| Farmacia | 8088 |
| Facturacion | 8089 |
| Eureka Server | 8761 |

## Rutas principales del Gateway

| Recurso | Ruta |
|---|---|
| Pacientes | `/api/pacientes/**` |
| Citas | `/api/citas/**` |
| Atenciones | `/api/atenciones/**` |
| Laboratorio | `/api/laboratorio/**` |
| Hospitalizaciones | `/api/hospitalizaciones/**` |
| Farmacia | `/api/farmacia/**` |
| Facturacion | `/api/facturacion/**` |

## Requisitos previos

- JDK compatible con los proyectos.
- PostgreSQL con la base de datos configurada.
- RabbitMQ disponible para los servicios que publican o consumen eventos.
- Maven Wrapper incluido en cada proyecto.

## Configuracion local

Cada microservicio tiene un archivo de configuracion de ejemplo `application.example.yaml` o `application.example.properties`.

1. Copia el archivo de ejemplo al nombre local correspondiente: `application.yaml` o `application.properties`.
2. Configura las credenciales locales de PostgreSQL y RabbitMQ.
3. Verifica las URL, los puertos y el nombre de la base de datos.
4. No publiques los archivos locales con credenciales reales.

Los archivos locales de configuracion se excluyen mediante `.gitignore`. Los archivos de ejemplo no deben contener secretos reales.

## Orden recomendado de inicio

1. PostgreSQL y RabbitMQ.
2. Eureka Server.
3. Pacientes.
4. Citas y los demas microservicios de negocio.
5. Notificaciones y Auditoria.
6. API Gateway.

Verifica el registro de las instancias en `http://localhost:8761`.

## Ejemplo de prueba

Para listar los pagos mediante el Gateway:

`GET http://localhost:8080/api/facturacion`

Para listar las citas mediante el Gateway:

`GET http://localhost:8080/api/citas`

## Compilacion y pruebas

Dentro de cada proyecto, ejecuta:

```powershell
.\mvnw.cmd clean verify
```

Revisa el resultado de Maven antes de considerar cada servicio validado.

## Seguridad

No se deben subir contrasenas, tokens, archivos `.env` ni configuraciones locales sensibles. Utiliza archivos de ejemplo y configura los secretos de forma local o mediante variables de entorno.

## Estructura

Cada carpeta de servicio contiene su propio proyecto Maven. El repositorio agrupa Eureka, el Gateway y los microservicios del sistema hospitalario.
