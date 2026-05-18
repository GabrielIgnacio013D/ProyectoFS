package cl.duoc.mesas_api.controller;

import cl.duoc.mesas_api.model.Mesa;
import cl.duoc.mesas_api.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private RestTemplate restTemplate; 

    // LEER TODAS
    @GetMapping
    public List<Mesa> obtenerTodas() {
        return mesaService.obtenerTodas();
    }

    // BUSCAR POR ID (¡Corregido, sin duplicados y con tu log para la terminal!)
    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtenerMesaPorId(@PathVariable Long id) {
        System.out.println("=========================================");
        System.out.println("[MESAS AWS] -> Recibiendo consulta externa de Reservas por ID: " + id); 
        System.out.println("=========================================");
        
        return mesaService.obtenerMesaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GUARDAR MESA
    @PostMapping
    public Mesa guardar(@RequestBody Mesa mesa) {
        return mesaService.guardar(mesa);
    }

    // ELIMINAR MESA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mesaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // DETALLE MESA CON PLATO
    @GetMapping("/{idMesa}/plato/{idPlato}")
    public ResponseEntity<String> obtenerDetalleMesaConPlato(@PathVariable Long idMesa, @PathVariable Long idPlato) {
        String detalle = mesaService.obtenerDetalleMesaConPlato(idMesa, idPlato);
        return ResponseEntity.ok(detalle);
    }

    // COCINA MÉTODOS UNIFICADOS (Se cambió el nombre del método para que no choque)
    @PostMapping("/api/cocina") 
    public ResponseEntity<String> recibirPedidoDeMiCompañeroDTO(@RequestBody cl.duoc.mesas_api.dto.PlatoCocinaDTO dto) {
        System.out.println("=========================================");
        System.out.println("¡ALERTA: LLEGÓ UN PEDIDO DESDE EL MICROSERVICIO DE MI COMPAÑERO!");
        System.out.println("Plato solicitado: " + dto.getPlato());
        System.out.println("Precio: $" + dto.getPrecio());
        System.out.println("Mesa asignada: " + dto.getMesa());
        System.out.println("=========================================");

        return ResponseEntity.ok("Pedido recibido con éxito en la cocina de Matías.");
    }

    @PostMapping("/cocina") 
    public ResponseEntity<String> recibirPedidoDeMiCompañeroMap(@RequestBody java.util.Map<String, Object> dto) {
        System.out.println("=========================================");
        System.out.println("¡ALERTA: LLEGÓ UN PEDIDO DESDE EL MICROSERVICIO DE MI COMPAÑERO!");
        System.out.println("Datos recibidos: " + dto.toString());
        System.out.println("=========================================");

        return ResponseEntity.ok("Pedido recibido con éxito en la cocina de Matías.");
    }

    // TEST DE COMUNICACIÓN CON MENÚ
    @GetMapping("/test-menu")
    public ResponseEntity<String> consultarCartasAlMenu() {
        System.out.println("=================================================");
        System.out.println("[MESAS] -> Alguien en una mesa está pidiendo la carta.");
        System.out.println("[MESAS] -> Conectando con tu servicio de Menú (Puerto 8081)...");
        System.out.println("=================================================");

        String urlMenu = "http://localhost:8081/api/menu/carta-interna"; 
        
        try {
            String respuesta = restTemplate.getForObject(urlMenu, String.class);
            return ResponseEntity.ok("¡Éxito! Tu servicio de Mesas leyó el Menú: " + respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al conectar con tu servicio-menu: " + e.getMessage());
        }
    }
}
