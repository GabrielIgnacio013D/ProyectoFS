package cl.duoc.menu_api.controller;

import cl.duoc.menu_api.model.Plato;
import cl.duoc.menu_api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<Plato> obtenerTodos() {
        return menuService.obtenerTodos();
    }

    @PostMapping
    public Plato guardar(@RequestBody Plato plato) {
        return menuService.guardar(plato);
    }

    // 🍔 ¡MÉTODO OPTIMIZADO PARA LA CONSULTA DE TU COMPAÑERO! 
    // Mantiene intacta la respuesta HTTP pero te muestra todo en consola
    @GetMapping("/{id}")
    public ResponseEntity<Plato> obtenerPorId(@PathVariable Long id) {
        System.out.println("=======================================================");
        System.out.println("[MENU] -> Recibiendo consulta externa por ID: " + id);
        System.out.println("[MENU] -> Buscando en la Base de Datos a través de MenuService...");
        System.out.println("=======================================================");

        return menuService.obtenerPorId(id)
                .map(plato -> {
                    // Si el plato existe en tu BD, imprime esto en tu consola sin romper el JSON de salida
                    System.out.println("[MENU] -> ¡ÉXITO! Solicitud procesada.");
                    System.out.println("[MENU] -> Plato enviado: " + plato.getNombre() + " | Precio: $" + plato.getPrecio());
                    System.out.println("=======================================================");
                    return ResponseEntity.ok(plato);
                })
                .orElseGet(() -> {
                    // Si tu compañero pide un ID que no tienes guardado
                    System.out.println("[MENU] -> ¡ALERTA! El ID " + id + " solicitado no existe en los registros.");
                    System.out.println("=======================================================");
                    return ResponseEntity.notFound().build();
                });
    }

    // Endpoint único para recibir el pedido de tu compañero usando un mapa dinámico (Intacto)
    @PostMapping("/pedidos") 
    public ResponseEntity<String> recibirPedidoDeMiCompañero(@RequestBody Map<String, Object> dto) {
        System.out.println("=========================================");
        System.out.println("¡ALERTA: LLEGÓ UN PEDIDO DESDE EL MICROSERVICIO DE MI COMPAÑERO!");
        
        // Extraemos los datos de forma segura usando las llaves del JSON que él te mande
        System.out.println("Plato solicitado: " + dto.getOrDefault("plato", "No especificado"));
        System.out.println("Precio: $" + dto.getOrDefault("precio", "0"));
        System.out.println("Mesa asignada: " + dto.getOrDefault("mesa", "No asignada"));
        System.out.println("=========================================");

        return ResponseEntity.ok("Pedido recibido con éxito en la cocina de Matías.");
    }

    // =========================================================================
    // NUEVO MÉTODO: Escucha la petición de tu propio servicio-mesas (Intacto)
    // =========================================================================
    @GetMapping("/carta-interna")
    public ResponseEntity<List<String>> obtenerMenuParaMesas() {
        System.out.println("=================================================");
        System.out.println("[MS-MENU] -> ¡Alerta! Tu servicio de Mesas me está pidiendo la carta.");
        System.out.println("[MS-MENU] -> Despachando los platos disponibles en tiempo real...");
        System.out.println("=================================================");

        List<String> platosPrueba = new ArrayList<>();
        platosPrueba.add("Pastel de Choclo (Simulado)");
        platosPrueba.add("Lomo a lo Pobre (Simulado)");
        platosPrueba.add("Paila Marina (Simulado)");

        return ResponseEntity.ok(platosPrueba);
    }
}
