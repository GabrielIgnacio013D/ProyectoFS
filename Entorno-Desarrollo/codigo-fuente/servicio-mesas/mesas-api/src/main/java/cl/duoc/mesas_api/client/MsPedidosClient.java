package cl.duoc.mesas_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-pedidos", url = "http://localhost:8083/api/pedidos")
public interface MsPedidosClient {

    // Aquí puedes agregar los métodos que necesites para consultar las órdenes de una mesa
    @GetMapping("/mesa/{idMesa}")
    Object obtenerPedidosPorMesa(@PathVariable("idMesa") Long idMesa);
}
