package eureka_api.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer; // <--- TE FALTA ESTA LÍNEA

@SpringBootApplication
@EnableEurekaServer // <--- Esto es lo que causa el error si no tiene el import arriba
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
