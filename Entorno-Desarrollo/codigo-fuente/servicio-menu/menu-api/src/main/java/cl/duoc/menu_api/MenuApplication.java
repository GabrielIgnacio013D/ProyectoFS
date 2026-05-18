package cl.duoc.menu_api; // Tu package real

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // <--- IMPORTA ESTO

@SpringBootApplication
@EnableDiscoveryClient // <--- AGREGA ESTA ANOTACIÓN
public class MenuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}

}