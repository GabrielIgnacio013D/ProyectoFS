package cl.duoc.mesas_api;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean; // <-- Agregado para habilitar el @Bean
import org.springframework.web.client.RestTemplate; // <-- Agregado para usar RestTemplate

@SpringBootApplication
@EnableFeignClients
public class MesasApiApplication {

    public static void main(String[] args) {
        System.out.println("Iniciando servicio de Mesas con Spring Boot Versión: " + SpringBootVersion.getVersion());
        SpringApplication.run(MesasApiApplication.class, args);
    }

    // Agregamos este método para que Spring Boot cree la herramienta RestTemplate.
    // Con esto, el @Autowired de tu MesaController encontrará la herramienta sin problemas.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
