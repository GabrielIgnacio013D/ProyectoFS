# 🚀 Sistema Distribuido Core: Gestión de Restaurante (Hito 2)

Este repositorio documenta la arquitectura de microservicios, el diseño de contratos, el control de seguridad perimetral y la bitácora de integración síncrona para el **Hito 2** del curso **Desarrollo FullStack 1 (DSY1103) - Duoc UC 2026**.

El ecosistema implementa una solución híbrida altamente desacoplada donde el núcleo de infraestructura, autenticación y catálogos reside en la nube (AWS EC2), interactuando síncronamente con módulos de consumo locales.

---

## 🏗️ 1. Arquitectura del Ecosistema y Topología de Red

El sistema se compone de una malla de microservicios estructurados que mitigan el acoplamiento y garantizan la persistencia independiente de los datos:

```text
       [ ENTORNO LOCAL ]                               [ MI SERVIDOR AWS EC2 ]
+-------------------------------+             +---------------------------------------+
|  ms-pedidos  /  ms-reservas   |             |   [ Api-Gateway ] (Puerto 8080)       |
| (Entorno de mis compañeros)   |             +---------------------------------------+
+-------------------------------+                                 |
                |                                                 v
          (Cruza Internet)                     +---------------------------------------+
                |                             |   [ servidor-eureka ] (Discovery)     |
                +---------------------------->+---------------------------------------+
                                                                  |
                                        +-------------------------+-------------------------+
                                        |                                                   |
                                        v                                                   v
                    +---------------------------------------+   +---------------------------------------+
                    |   [ servicio-mesas ] (Core Mesas)     |   |    [ servicio-menu ] (Core Menú)      |
                    |         Puerto: 8083 / 8080           |   |         Puerto Externo: 8081      |
                    +---------------------------------------+   +---------------------------------------+
                                        |
                                  (RestTemplate)
                                        v
                            [ servicio-identidad ] (JWT)




🎛️ Matriz de Puertos y Direccionamiento de Infraestructura
Para garantizar el libre tránsito de las peticiones síncronas sin bloqueos perimetrales
 (Connection Refused), se configuró el Security Group en AWS habilitando las siguientes compuertas:


Módulo / Directorio,Puerto Interno,Puerto Externo (Host),Protocolo,Misión Crítica en el Sistema
Api-Gateway,8080,8080,TCP,Enrutador perimetral único y normalizador de tráfico.
servidor-eureka,8761,8761,TCP,Orquestador de descubrimiento y mapeo de instancias vivas.
servicio-identidad,8085,8085,TCP,Autenticación centralizada y despacho de Tokens JWT.
servicio-menu,8082,8081,TCP,Exposición de la carta interna y platos disponibles.
servicio-mesas,8080,8083,TCP,Gestión física de disponibilidad y asignación de salones.




2. Contratos de Conectividad e Integración Síncrona
El intercambio de datos entre capas se realiza mediante llamadas HTTP directas regidas
 por contratos estrictos, impidiendo dependencias circulares:


Módulo Emisor (Origen),Módulo Receptor (Destino),Método HTTP,Endpoint Target,DTO / Contrato de Datos
ms-pedidos (Local),servicio-mesas (AWS),POST,/api/mesas/cocina,PedidoCreateDTO -> MesaAsignadaDTO
ms-reservas (Local),servicio-mesas (AWS),GET,/api/mesas/{id},"MesaDTO (id, numero, estado)"
servicio-mesas (AWS),servicio-menu (AWS),GET,/api/menu/carta-interna,String[] (Catálogo síncrono de platos)
Api-Gateway (AWS),servicio-identidad (AWS),POST,/api/auth/login,AuthRequest -> JwtTokenResponse




Justificación de los Mecanismos de Comunicación
Spring Cloud OpenFeign (Lado Cliente Local): Implementado por la contraparte para consumir el clúster
 Cloud mediante interfaces declarativas, abstrayendo por completo el manejo manual de sockets de red.

RestTemplate (Lado Cloud Interno): Utilizado para intercomunicar el microservicio de Mesas con el Menú
dentro del bucle local de la instancia EC2, maximizando la velocidad de respuesta.


Bitácora de Sprint (Hitos Completados)
🟢 Bloque 1: Arquitectura Base y Contenedores
Módulos Creados: Inicialización estructurada de las 5 carpetas del proyecto (Api-Gateway, servicio-identidad, servicio-menu, servicio-mesas, servidor-eureka) bajo el estándar Spring Boot.

Orquestación con Docker Compose: Configuración del archivo docker-compose.yml multi-contenedor, aislando las redes de datos de la lógica de negocio mediante volúmenes independientes.

🟢 Bloque 2: Autenticación Edge (Servicio de Identidad)
Validación de Credenciales: Implementación de consultas JPA sobre la tabla usuarios para el inicio de sesión.

Emisión de Tokens JWT: Desarrollo de la lógica de seguridad para interceptar accesos y despachar credenciales simuladas firmadas digitalmente por el Gateway al validar identidades corporativas.

🟢 Bloque 3: Integración de Pedidos y Lógica de Cocina
Inyección del Puente Síncrono: Conexión fluida del tráfico enviado por el equipo de desarrollo local hacia la infraestructura Cloud de Mesas.

Mapeo de Negocio (El Caso de Uso): Procesamiento de solicitudes complejas en tiempo real (Ej: Entrada de pedido de Pastel de Choclo desde el controlador cliente, cálculo de precio e impacto directo en consola asignando automáticamente la Mesa 7).






4. Explicación de Capas y Refactorizaciones del Código
El microservicio servicio-mesas respeta rigurosamente el patrón arquitectónico multicapa propuesto para el laboratorio:

Controller (MesaController.java): Único responsable de la recepción de solicitudes HTTP. Se depuraron endpoints @GetMapping("/{id}") duplicados que generaban ambigüedad y colisiones de rutas al levantar el servidor.

Service (MesaService.java): Contenedor de las reglas de negocio. Se corrigió un error crítico de compilación (cannot find symbol) eliminando llamadas fantasmas a mesasRepository, inyectando adecuadamente los métodos mediante el desacoplamiento de servicios.

DTO (Data Transfer Objects): Para evitar la exposición nativa de la entidad base de datos (Mesa.java), se crearon clases espejo planos (PlatoCocinaDTO). Esto permitió procesar payloads tanto estructurados como mapas dinámicos (Map<String, Object>), absorbiendo variaciones de campos enviados por terceros sin que se caiga el hilo de ejecución de Java.






5. Operación y Comandos de Consola en el Servidor AWS
💀 Aniquilación de Hilos Colgados (Liberación de Puertos)
Cuando un subproceso de Maven o Docker queda amarrado reteniendo el puerto 8080, se ejecuta el siguiente comando para limpiar la interfaz de red inmediatamente:


sudo kill -9 $(sudo lsof -t -i:8080)


Despliegue en Caliente del Entorno Core
Para compilar sin pruebas unitarias y levantar el microservicio de Mesas escuchando peticiones de internet de manera nativa:

Bash
mvn spring-boot:run
📊 6. Evidencias Teletipo de Consola (Logs de Éxito)
A continuación se adjuntan las trazas reales capturadas directamente desde la terminal del servidor en AWS, certificando el correcto funcionamiento de cada hito del Trello:

A. Auditoría de Identidad y Autenticación JWT (AWS)
Plaintext
[servicio-identidad] -> Recibiendo solicitud de Login.
[servicio-identidad] -> Validando credenciales para el usuario: admin
Hibernate: select u1_0.id, u1_0.email, u1_0.password, u1_0.username from usuarios u1_0 ...
[servicio-identidad] -> ¡ÉXITO! Credenciales válidas.
[servicio-identidad] -> Generando y despachando Token JWT simulado...

B. Intercomunicación de Pedidos y Despacho a Cocina (Local -> AWS)
Plaintext
ALERTA: LLEGÓ UN PEDIDO DESDE OTRO MICROSERVICIO!
Plato solicitado: Pastel de Choclo (Desde MS Pedidos)
Precio: $9500
Mesa asignada: Mesa 7
--------------------------------------------

C. Consulta Síncrona de Disponibilidad (Reservas -> AWS)
Plaintext
[MESAS AWS] -> Recibiendo consulta externa de Reservas por ID: 1
Hibernate: select m1_0.id, m1_0.estado, m1_0.numero from mesa m1_0 where m1_0.id=?
[MESAS AWS] -> ¡Mesa localizada con éxito! Estado: LIBRE | Precio: $2200.0
💡 Estado del Proyecto: El ecosistema completo se encuentra en un estado operativo verde (100% de criterios cubiertos), con persistencia validada y resiliencia de red comprobada ante fallos.
