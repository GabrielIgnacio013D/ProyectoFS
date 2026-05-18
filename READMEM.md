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
