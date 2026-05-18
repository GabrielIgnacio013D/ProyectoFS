package cl.duoc.identidad_api.controller;

import cl.duoc.identidad_api.model.Usuario;
import cl.duoc.identidad_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    // --- ENDPOINT DE LOGIN CONECTADO AL SERVICE CON ALERTAS VISUALES ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("\n=======================================================");
        System.out.println("[IDENTIDAD] -> Recibiendo solicitud de Login.");
        System.out.println("[IDENTIDAD] -> Validando credenciales para el usuario: " + loginRequest.getUsername());
        System.out.println("=======================================================");

        // El controlador le pasa los datos al service para que haga la validación
        Optional<Usuario> usuarioValido = usuarioService.autenticar(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
        );

        if (usuarioValido.isPresent()) {
            System.out.println("=======================================================");
            System.out.println("[IDENTIDAD] -> ¡ÉXITO! Credenciales válidas.");
            System.out.println("[IDENTIDAD] -> Generando y despachando Token JWT simulado...");
            System.out.println("=======================================================\n");

            response.put("status", "success");
            response.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTg5OTk5OTk5OX0");
            response.put("username", usuarioValido.get().getUsername());
            response.put("message", "Autenticación exitosa. Token generado.");
            
            return ResponseEntity.ok(response);
        } else {
            System.out.println("=======================================================");
            System.out.println("[IDENTIDAD] -> ¡ALERTA! Credenciales incorrectas.");
            System.out.println("[IDENTIDAD] -> Acceso denegado para: " + loginRequest.getUsername());
            System.out.println("=======================================================\n");

            response.put("status", "error");
            response.put("message", "Credenciales incorrectas o usuario no encontrado");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}