package cl.duoc.mesas_api.client;

import cl.duoc.mesas_api.dto.PlatoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-menu", url = "http://localhost:8081/api/menu")
public interface MenuClient {

    @GetMapping("/{id}")
    PlatoDTO obtenerPlatoPorId(@PathVariable("id") Long id);
}
